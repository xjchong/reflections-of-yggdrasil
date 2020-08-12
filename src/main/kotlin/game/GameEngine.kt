package game

import attributes.EntityTime
import commands.ExecuteAiPlans
import commands.ExecutePlayerInput
import entity.AnyEntity
import entity.getAttribute
import entity.hasFacet
import entity.time
import facets.AiControllable
import facets.PlayerControllable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Pass
import utilities.DebugConfig
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

object GameEngine : Engine<GameContext>, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    // The time that new entities will spawn in with.
    var gameTime: Long = 0
        private set

    private val inputEventQueue: MutableList<GameContext> = mutableListOf() // Stores input events in case the player is faster than the update can run.
    private val isUpdating: AtomicBoolean = AtomicBoolean(false) // Used in ensuring updates run sequentially.

    // Compare by the in game time, and then the real time of update to ensure correct entity order.
    private val gameTimeComp = compareBy<AnyEntity> {
        it.getAttribute(EntityTime::class)?.nextUpdateTime?.get() ?: Long.MAX_VALUE
    }.thenBy {
        it.getAttribute(EntityTime::class)?.lastUpdatedTime?.get() ?: Long.MAX_VALUE
    }

    private val entities: MutableList<AnyEntity> = mutableListOf()

    @Synchronized
    override fun addEntity(entity: AnyEntity) {
        if (!entity.hasFacet<AiControllable>() && !entity.hasFacet<PlayerControllable>()) return
        entities.binaryInsert(entity)
    }

    @Synchronized
    override fun removeEntity(entity: AnyEntity) {
        entities.remove(entity)
    }

    @Synchronized
    override fun update(context: GameContext): Job {
        if (isUpdating.get()) {
            inputEventQueue.add(context)
            return Job()
        }

        isUpdating.set(true)

        val updateStartTime = System.currentTimeMillis()

        return launch {
            do {
                if (entities.isEmpty()) break

                val firstEntity = entities.first()

                if (firstEntity.hasFacet<PlayerControllable>()) {
                    if (firstEntity.executeCommand(ExecutePlayerInput(context, firstEntity, context.event)) == Pass) {
                        isUpdating.set(false)
                        return@launch
                    }
                } else {
                    firstEntity.update(context)
                    firstEntity.executeCommand(ExecuteAiPlans(context, firstEntity))
                }

                if (!context.inBackground) {
                    firstEntity.getAttribute(EntityTime::class)?.flagUpdatedAt(System.nanoTime())
                    entities.reSortFirst()
                }
            } while (isPlayerNotFirst())

            // The first entity should be an input receiving one. Let it update now before asking for input.
            val inputEntity = entities.first()
            gameTime = inputEntity.time
            inputEntity.update(context)

            // Print out the time it took to run the update loop, if the debug command is enabled.
            if (DebugConfig.shouldLogUpdateSpeed) println("${System.currentTimeMillis() - updateStartTime}")

            isUpdating.set(false)

            if (inputEventQueue.isNotEmpty()) {
                val nextInputEvent = inputEventQueue.removeAt(0)
                update(nextInputEvent)
            }
        }
    }

    @Synchronized
    private fun isPlayerNotFirst(): Boolean {
        val firstEntity = entities.first()
        return firstEntity.hasFacet<PlayerControllable>().not()
    }

    @Synchronized
    private fun MutableList<AnyEntity>.reSortFirst() {
        val firstEntity = removeAt(0)
        binaryInsert(firstEntity)
    }

    @Synchronized
    private fun MutableList<AnyEntity>.binaryInsert(entity: AnyEntity) {
        val invertedInsertionPoint = binarySearch(entity, gameTimeComp)
        val actualInsertionPoint = -(invertedInsertionPoint + 1)

        if (actualInsertionPoint < 0) return // Sanity check. Shouldn't be necessary.

        add(actualInsertionPoint, entity)
    }
}
