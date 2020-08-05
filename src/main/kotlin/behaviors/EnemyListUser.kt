package behaviors

import attributes.EnemyList
import attributes.Senses
import entity.AnyEntity
import entity.getAttribute
import entity.isAlliedWith
import game.GameContext

object EnemyListUser : ForegroundBehavior(EnemyList::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val senses = entity.getAttribute(Senses::class) ?: return false
        val sensedEnemies = senses.sensedEntities.filter {
            !entity.isAlliedWith(it)
        }

        entity.getAttribute(EnemyList::class)?.updateEnemies(sensedEnemies)

        return true
    }
}