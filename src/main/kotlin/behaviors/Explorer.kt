package behaviors

import attributes.ExplorerDetails
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

object Explorer : ForegroundBehavior(ExplorerDetails::class, Goals::class) {

    const val GOAL_KEY = "Explore"

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val details = entity.getAttribute(ExplorerDetails::class) ?: return false
        val position = entity.position

        if (position.isUnknown) return false

        details.logVisit(position)

        val nextPosition = position.neighbors().filter {
            val block = context.world.fetchBlockAt(it).optional
            block != null && !block.isWall
        }.minBy {
            details.visited.getOrDefault(it, 0)
        } ?: return false

        return entity.addExploreGoal(context, nextPosition)
    }

    private fun AnyEntity.addExploreGoal(context: GameContext, nextPosition: Position3D): Boolean {
        val goals = getAttribute(Goals::class) ?: return false

        return goals.add(Goal(GOAL_KEY, 30) {
            executeCommand(Move(context, this, nextPosition))
        })
    }
}