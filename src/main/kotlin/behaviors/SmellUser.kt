package behaviors

import attributes.EnemyList
import attributes.Smell
import entity.*
import game.GameContext

object SmellUser : ForegroundBehavior(Smell::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val smell = entity.getAttribute(Smell::class) ?: return false

        smell.smellablePositions.clear()
        smell.smellablePositions.addAll(context.world.findSmellablePositionsFor(entity.position, smell.radius))

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