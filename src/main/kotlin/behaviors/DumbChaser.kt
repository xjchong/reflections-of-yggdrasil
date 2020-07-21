package behaviors

import commands.Move
import entity.AnyGameEntity
import entity.Player
import entity.executeBlockingCommand
import entity.position
import extensions.neighbors
import extensions.optional
import game.GameContext

object DumbChaser : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyGameEntity, context: GameContext): Boolean {
        val world = context.world
        var isChasing = false

        for (visiblePos in world.findVisiblePositionsFor(entity)) {
            world.fetchBlockAt(visiblePos).ifPresent { block ->
                if (block.entities.any { it.type == Player }) {
                    val (visibleX, visibleY) = visiblePos
                    val (entityX, entityY) = entity.position

                    val idealX = when {
                        visibleX > entityX -> 1
                        visibleX < entityX -> -1
                        else -> 0
                    }

                    val idealY = when {
                        visibleY > entityY -> 1
                        visibleY < entityY -> -1
                        else -> 0
                    }

                    val idealPosition = entity.position.withRelativeX(idealX).withRelativeY(idealY)
                    var nextPosition = idealPosition

                    if (idealPosition != visiblePos
                            && world.fetchBlockAt(idealPosition).optional?.isObstructed == true) {
                        // The ideal position is blocked, and not by the target, so try other potentials.
                        val potentialMoves = entity.position.neighbors().filter {
                            // Get a position in the general direction of the target.
                            when {
                                it == idealPosition -> false
                                visiblePos > entity.position -> it > entity.position
                                else -> it < entity.position
                            }
                        }.filter {
                            world.fetchBlockAt(it).optional?.isObstructed == false
                        }

                        potentialMoves.firstOrNull()?.let { nextPosition = it }
                    }

                    entity.executeBlockingCommand(Move(context, entity, nextPosition))
                    isChasing = true
                }
            }
            if (isChasing) break
        }

        return isChasing
    }
}