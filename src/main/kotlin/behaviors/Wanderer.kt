package behaviors

import commands.Move
import entity.position
import extensions.neighbors
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType


object Wanderer : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.inBackground) return true

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