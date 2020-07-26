package behaviors

import attributes.EnemyList
import attributes.Vision
import entity.*
import game.GameContext

object VisionUser : ForegroundBehavior(Vision::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val vision = entity.getAttribute(Vision::class) ?: return true

        vision.visiblePositions.clear()
        vision.visiblePositions.addAll(context.world.findVisiblePositionsFor(entity.position, vision.radius))

        entity.getAttribute(EnemyList::class)?.run {
            val newEnemies: MutableList<AnyEntity> = mutableListOf()
            entity.sensedPositions.forEach { sensedPos ->
                newEnemies.addAll((context.world.fetchEntitiesAt(sensedPos).filter { sensedEntity ->
                    !entity.isAlliedWith(sensedEntity)
                }))
            }

            updateEnemies(newEnemies)
        }

        return true
    }
}