package behaviors

import attributes.Goal
import attributes.Goals
import commands.Move
import entity.AnyEntity
import entity.getAttribute
import entity.position
import extensions.neighbors
import extensions.optional
import game.GameContext
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
        val goals = getAttribute(Goals::class) ?: return false

        return goals.list.add(Goal(GOAL_KEY, 20) {
            executeCommand(Move(context, this, nextPosition))
        })
    }
}
