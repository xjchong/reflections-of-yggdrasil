package game

import behaviors.InputReceiving
import commands.Input
import entity.AnyEntity
import kotlinx.coroutines.*
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Pass
import kotlin.coroutines.CoroutineContext

class GameEngine(
    override val coroutineContext: CoroutineContext = Dispatchers.Default
) : Engine<GameContext>, CoroutineScope {

    companion object {
        const val PRIORITY_LOW = 0
        const val PRIORITY_DEFAULT = 1
        const val PRIORITY_HIGH = 2
    }

    private val lowPriorityEntities = mutableListOf<AnyEntity>()
    private val defaultPriorityEntities = mutableListOf<AnyEntity>()
    private val highPriorityEntities = mutableListOf<AnyEntity>()
    private var inputReceivingEntity: AnyEntity? = null

    @Synchronized
    override fun addEntity(entity: AnyEntity) {
        addEntityWithPriority(entity)
    }

    @Synchronized fun addEntityWithPriority(entity: AnyEntity, priority: Int = PRIORITY_DEFAULT) {
        when (priority) {
            PRIORITY_LOW -> lowPriorityEntities.add(entity)
            PRIORITY_DEFAULT -> defaultPriorityEntities.add(entity)
            PRIORITY_HIGH -> highPriorityEntities.add(entity)
        }
    }

    @Synchronized
    override fun removeEntity(entity: AnyEntity) {
        if (defaultPriorityEntities.remove(entity)) return
        if (highPriorityEntities.remove(entity)) return
        if (lowPriorityEntities.remove(entity)) return
        if (inputReceivingEntity?.id == entity.id) {
            inputReceivingEntity = null
        }
    }

    @Synchronized
    override fun update(context: GameContext): Job {
        return launch {
            inputReceivingEntity?.run {
                if (this.executeCommand(Input(context, this, context.event)) == Pass) {
                    return@launch
                }
            }
            highPriorityEntities.filter { it.needsUpdate }.map {
                async { it.update(context) }
            }.awaitAll()
            defaultPriorityEntities.filter { it.needsUpdate }.map {
                async { it.update(context) }
            }.awaitAll()
            lowPriorityEntities.filter { it.needsUpdate }.map {
                async { it.update(context) }
            }.awaitAll()
            async {
                inputReceivingEntity?.update(context)
            }.await()
        }
    }

    fun setInputReceivingEntity(entity: AnyEntity) {
        if (!entity.facets.contains(InputReceiving)) {
            throw IllegalArgumentException("Can't set $entity as the input receiving entity for the game engine. " +
                    "Does it have ${InputReceiving::class.simpleName}?")
        }

        inputReceivingEntity = entity
    }
}
