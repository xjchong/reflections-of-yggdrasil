package behaviors

import attributes.EnemyList
import attributes.Senses
import entity.GameEntity
import entity.getAttribute
import entity.isEnemiesWith
import game.GameContext

object EnemyListUser : ForegroundBehavior(EnemyList::class) {

    override suspend fun foregroundUpdate(entity: GameEntity, context: GameContext): Boolean {
        val senses = entity.getAttribute(Senses::class) ?: return false
        val sensedEnemies = senses.sensedEntities.filter {
            entity.isEnemiesWith(it)
        }

        entity.getAttribute(EnemyList::class)?.updateEnemies(sensedEnemies)

        return true
    }
}