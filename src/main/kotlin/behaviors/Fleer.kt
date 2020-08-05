package behaviors

import attributes.CombatStats
import attributes.Goal
import attributes.Goals
import attributes.Presence
import commands.Move
import entity.*
import extensions.neighbors
import game.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.zircon.api.data.Position3D

object Fleer : ForegroundBehavior(Goals::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        var enemy: AnyEntity?

        for (sensedPos in entity.sensedPositions.minus(entity.position)) {
            enemy = world.fetchEntitiesAt(sensedPos).firstOrNull {
                !entity.isAlliedWith(it)
            }

            if (enemy != null) {
                return entity.addFleeGoal(context, enemy)
            }
        }

        return false
    }

    private fun AnyEntity.addFleeGoal(context: GameContext, target: AnyEntity): Boolean {
        val targetPresence = target.getAttribute(Presence::class) ?: return false
        val combatStats = getAttribute(CombatStats::class) ?: return false
        var nextPosition = Position3D.unknown()
        var lowestAvoidanceVal: Int? = null

        for (neighbor in position.neighbors()) {
            val avoidanceVal = targetPresence.avoidanceMap[neighbor] ?: continue

            if (lowestAvoidanceVal == null || avoidanceVal < lowestAvoidanceVal) {
                nextPosition = neighbor
                lowestAvoidanceVal = avoidanceVal
            }
        }

        if (nextPosition == Position3D.unknown()) return false

        return getAttribute(Goals::class)?.list?.add(Goal(
                "Flee",
                100 - combatStats.health / combatStats.maxHealth * 100) {
            executeBlockingCommand(Move(context, this, nextPosition)) == Consumed
        }) == true
    }
}