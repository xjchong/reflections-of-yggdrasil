package game

import kotlinx.coroutines.*
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import kotlin.coroutines.CoroutineContext

class GameEngine<T : GameContext>(
    override val coroutineContext: CoroutineContext = Dispatchers.Default
) : Engine<T>, CoroutineScope {

    companion object {
        const val PRIORITY_LOW = 0
        const val PRIORITY_DEFAULT = 1
        const val PRIORITY_HIGH = 2
    }

    private val lowPriorityEntities = mutableListOf<Entity<EntityType, T>>()
    private val defaultPriorityEntities = mutableListOf<Entity<EntityType, T>>()
    private val highPriorityEntities = mutableListOf<Entity<EntityType, T>>()

    @Synchronized
    override fun addEntity(entity: Entity<EntityType, T>) {
        addEntityWithPriority(entity)
    }

    @Synchronized fun addEntityWithPriority(entity: Entity<EntityType, T>, priority: Int = PRIORITY_DEFAULT) {
        when (priority) {
            PRIORITY_LOW -> lowPriorityEntities.add(entity)
            PRIORITY_DEFAULT -> defaultPriorityEntities.add(entity)
            PRIORITY_HIGH -> highPriorityEntities.add(entity)
        }
    }

    @Synchronized
    override fun removeEntity(entity: Entity<EntityType, T>) {
        if (defaultPriorityEntities.remove(entity)) return
        if (highPriorityEntities.remove(entity)) return
        if (lowPriorityEntities.remove(entity)) return
    }

    @Synchronized
    override fun update(context: T): Job {
        return launch {
            highPriorityEntities.filter { it.needsUpdate }.map {
                async { it.update(context) }
            }.awaitAll()
            defaultPriorityEntities.filter { it.needsUpdate }.map {
                async { it.update(context) }
            }.awaitAll()
            lowPriorityEntities.filter { it.needsUpdate }.map {
                async { it.update(context) }
            }.awaitAll()
        }
    }
}
