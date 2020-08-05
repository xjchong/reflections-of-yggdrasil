package behaviors

import attributes.Senses
import entity.AnyEntity
import entity.getAttribute
import entity.position
import game.GameContext

object SensoryUser : ForegroundBehavior(Senses::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val senses = entity.getAttribute(Senses::class) ?: return false

        senses.visiblePositions.clear()
        if (senses.vision > 0) {
            senses.visiblePositions.addAll(context.world.findVisiblePositionsFor(entity.position, senses.vision))
        }

        senses.smellablePositions.clear()
        if (senses.smell > 0) {
            senses.smellablePositions.addAll(context.world.findSmellablePositionsFor(entity.position, senses.smell))
        }

        senses.sensedEntities.clear()
        senses.sensedPositions.forEach { sensedPos ->
            senses.sensedEntities.addAll(context.world.fetchEntitiesAt(sensedPos))
        }

        return true
    }
}