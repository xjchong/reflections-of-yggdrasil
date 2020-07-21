package behaviors

import commands.Move
import entity.Player
import entity.executeBlockingCommand
import entity.position
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType

object DumbChaser : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val world = context.world
        var isChasing = false

        for (visiblePos in world.findVisiblePositionsFor(entity)) {
            world.fetchBlockAt(visiblePos).ifPresent { block ->
                if (block.entities.any { it.type == Player }) {
                    val (visibleX, visibleY) = visiblePos
                    val (entityX, entityY) = entity.position

                    val x = when {
                        visibleX > entityX -> 1
                        visibleX < entityX -> -1
                        else -> 0
                    }

                    val y = when {
                        visibleY > entityY -> 1
                        visibleY < entityY -> -1
                        else -> 0
                    }

                    val nextPosition = entity.position.withRelativeX(x).withRelativeY(y)

                    entity.executeBlockingCommand(
                            Move(context, entity, nextPosition))
                    isChasing = true
                }
            }
        }

        return isChasing
    }
}