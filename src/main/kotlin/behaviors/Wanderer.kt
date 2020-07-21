package behaviors

import commands.Move
import entity.AnyGameEntity
import entity.position
import extensions.neighbors
import extensions.optional
import game.GameContext


object Wanderer : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyGameEntity, context: GameContext): Boolean {
        val position = entity.position

        if (!position.isUnknown) {
            val nextPosition = position.neighbors().firstOrNull { potentialPos ->
                val block = context.world.fetchBlockAt(potentialPos).optional
                block != null && !block.isWall
            }

            nextPosition?.let {
                entity.executeCommand(Move(context, entity, it))
                return true
            }
        }

        return false
    }
}
