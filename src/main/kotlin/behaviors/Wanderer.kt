package behaviors

import attributes.Goal
import attributes.Goals
import commands.AttemptAnyAction
import commands.Move
import entity.AnyEntity
import entity.executeBlockingCommand
import entity.getAttribute
import entity.position
import extensions.neighbors
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.zircon.api.data.Position3D


object Wanderer : ForegroundBehavior(Goals::class) {

    val GOAL_KEY = "Wander"

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val position = entity.position

        if (!position.isUnknown) {
            val nextPosition = position.neighbors().firstOrNull { potentialPos ->
                val block = context.world.fetchBlockAt(potentialPos).optional
                block != null && !block.isWall
            }

            if (nextPosition != null) {
                return entity.addWanderGoal(context, nextPosition)
            }
        }

        return false
    }

    private fun AnyEntity.addWanderGoal(context: GameContext, nextPosition: Position3D): Boolean {
        return getAttribute(Goals::class)?.list?.add(Goal(GOAL_KEY, 20) {
            executeBlockingCommand(AttemptAnyAction(context, this, nextPosition)) == Consumed
                    || executeBlockingCommand(Move(context, this, nextPosition)) == Consumed
        }) == true
    }
}
