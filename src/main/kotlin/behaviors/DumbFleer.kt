package behaviors

import attributes.CombatStats
import attributes.Presence
import commands.Move
import entity.*
import extensions.neighbors
import game.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.zircon.api.data.Position3D


object DumbFleer : ForegroundBehavior(Presence::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val combatStats = entity.getAttribute(CombatStats::class) ?: return false

        // TODO: Right now, entities flee if below 0.3 health. But some monster should flee at different thresholds, or even not at all. This constant should be encapsulated in an attribute perhaps.
        if (combatStats.health.toDouble() / combatStats.maxHealth.toDouble() > 0.3) return false

        val world = context.world
        var enemy: AnyEntity? = null

        for (sensedPos in entity.sensedPositions.minus(entity.position)) {
            world.fetchBlockAt(sensedPos).ifPresent { block ->
                block.entities.firstOrNull { !entity.isAlliedWith(it) }?.let { enemy = it }
            }

            if (enemy != null) break
        }

        return fleeFromEnemy(context, entity, enemy)
    }

    private suspend fun fleeFromEnemy(context: GameContext, fleer: AnyEntity, enemy: AnyEntity?): Boolean {
        if (enemy == null) return false
        val enemyPresence = enemy.getAttribute(Presence::class) ?: return false
        var nextPosition = Position3D.unknown()
        var lowestAvoidanceVal: Int? = null

        for (neighbor in fleer.position.neighbors()) {
            val avoidanceVal = enemyPresence.avoidanceMap[neighbor] ?: continue

            if (lowestAvoidanceVal == null || avoidanceVal < lowestAvoidanceVal) {
                nextPosition = neighbor
                lowestAvoidanceVal = avoidanceVal
            }
        }

        if (nextPosition == Position3D.unknown()) return false

        return fleer.executeCommand(Move(context, fleer, nextPosition)) == Consumed
    }
}