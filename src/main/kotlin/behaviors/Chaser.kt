package behaviors

import attributes.Goal
import attributes.Goals
import attributes.Presence
import commands.Move
import entity.*
import extensions.neighbors
import game.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.zircon.api.data.Position3D

object Chaser : ForegroundBehavior(Goals::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        var enemy: AnyEntity? = null

        for (sensedPos in entity.sensedPositions.minus(entity.position)) {
            enemy = world.fetchEntitiesAt(sensedPos).firstOrNull {
                !entity.isAlliedWith(it)
            }

            if (enemy != null) {
                return entity.addChaseGoal(context, enemy)
            }
        }

        return false
    }

    private suspend fun AnyEntity.addChaseGoal(context: GameContext, target: AnyEntity): Boolean {
        val targetPresence = target.getAttribute(Presence::class) ?: return false
        var nextPosition = Position3D.unknown()
        var lowestApproachVal: Int? = null

        for (neighbor in position.neighbors()) {
            val approachVal = targetPresence.approachMap[neighbor] ?: continue

            if (lowestApproachVal == null || approachVal < lowestApproachVal) {
                nextPosition = neighbor
                lowestApproachVal = approachVal
            }
        }

        if (nextPosition == Position3D.unknown()) return false

        return getAttribute(Goals::class)?.list?.add(Goal("Chase", 70) {
            executeBlockingCommand(Move(context, this, nextPosition)) == Consumed
        }) == true
    }
}