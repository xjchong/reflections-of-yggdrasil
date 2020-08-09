package game

import commands.Input
import entity.AnyEntity
import entity.hasFacet
import facets.active.InputReceiving
import kotlinx.coroutines.*
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Pass
import utilities.DebugConfig
import kotlin.coroutines.CoroutineContext

class GameEngine(
    override val coroutineContext: CoroutineContext = Dispatchers.Default
) : Engine<GameContext>, CoroutineScope {

    private var inputReceivingEntity: AnyEntity? = null
    private val inputReceivingEntities: MutableList<AnyEntity> = mutableListOf()
    private val nonInputReceivingEntities: MutableList<AnyEntity> = mutableListOf()

    @Synchronized
    override fun addEntity(entity: AnyEntity) {
        if (entity.hasBehaviors.not()) return // Dangerous if there are entities that can have behaviors added dynamically.

        if (entity.hasFacet<InputReceiving>()) {
            inputReceivingEntities.add(entity)
        } else {
            nonInputReceivingEntities.add(entity)
        }
    }

    @Synchronized
    override fun removeEntity(entity: AnyEntity) {
        inputReceivingEntities.remove(entity)
    }

    @Synchronized
    override fun update(context: GameContext): Job {
        val updateStartTime = System.currentTimeMillis()
        return launch {
            for (inputEntity in inputReceivingEntities) {
                if (inputEntity.executeCommand(Input(context, inputEntity, context.event)) == Pass) {
                    return@launch
                }
            }

            nonInputReceivingEntities.filter { it.needsUpdate }.map {
                async { it.update(context) }
            }.awaitAll()

            inputReceivingEntities.filter { it.needsUpdate }.map {
                async { it.update(context) }
            }

            if (DebugConfig.shouldLogUpdateSpeed) println("${System.currentTimeMillis() - updateStartTime}")
        }
    }
}
