package behaviors

import attributes.Senses
import attributes.SensoryMemory
import entity.GameEntity
import entity.getAttribute
import entity.position
import game.GameContext

object SensoryUser : ForegroundBehavior(Senses::class) {

    override suspend fun foregroundUpdate(entity: GameEntity, context: GameContext): Boolean {
        val senses = entity.getAttribute(Senses::class) ?: return false

        if (senses.vision > 0) {
            senses.visiblePositions = (context.world.findVisiblePositionsFor(entity.position, senses.vision)).toSet()
        }

        if (senses.smell > 0) {
            senses.smellablePositions = (context.world.findSmellablePositionsFor(entity.position, senses.smell)).toSet()
        }

        val sensedEntities = mutableListOf<GameEntity>()
        senses.sensedPositions.forEach { sensedPos ->
            sensedEntities.addAll(context.world.fetchEntitiesAt(sensedPos))
        }

        senses.sensedEntities = sensedEntities

        entity.recordSensoryMemories(context)

        return true
    }

    private fun GameEntity.recordSensoryMemories(context: GameContext) {
        val senses = getAttribute(Senses::class) ?: return
        val memory = getAttribute(SensoryMemory::class) ?: return
        val world = context.world

        senses.sensedPositions.forEach { sensedPos ->
            world.fetchBlockAt(sensedPos).ifPresent { block ->
                memory.remember(sensedPos, world.turn, block.entities.toList())
                world.remember(memory, this, block)
            }
        }
    }
}