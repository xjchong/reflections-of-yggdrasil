package behaviors

import attributes.CombatStats
import attributes.KillTarget
import commands.AttemptAnyAction
import commands.Move
import entity.*
import extensions.neighbors
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.Pass
import org.hexworks.zircon.api.data.Position3D

object DumbChaser : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        var isChasing = false

        for (visiblePos in world.findVisiblePositionsFor(entity).minus(entity.position)) {
            world.fetchBlockAt(visiblePos).ifPresent { block ->
                block.entities.firstOrNull {
                            it.findAttribute(CombatStats::class).isPresent
                                    && !entity.isAlliedWith(it) }?.let { target ->
                    val targetPos = target.position
                    val entityPos = entity.position
                    val greedyPos = getGreedyPosition(entityPos, targetPos)
                    var nextPosition = greedyPos

                    if (greedyPos != target.position
                            && world.fetchBlockAt(greedyPos).optional?.isObstructed == true) {

                        // The greedy position is blocked, and not by the target, so try other potential positions.
                        val potentialMoves = entityPos.neighbors().filter { potentialPos ->
                            if (world.fetchBlockAt(potentialPos).optional?.isObstructed != false) {
                                return@filter false
                            }

                            when { // Get a position in the general direction of the target.
                                potentialPos == greedyPos -> false // This position was already determined to be blocked.
                                targetPos > entityPos -> potentialPos > entityPos
                                else -> potentialPos < entityPos
                            }
                        }

                        potentialMoves.firstOrNull()?.let { nextPosition = it }
                    }

                    entity.getAttribute(KillTarget::class)?.target = target

                    if (entity.executeBlockingCommand(AttemptAnyAction(context, entity, nextPosition)) == Pass) {
                        entity.executeBlockingCommand(Move(context, entity, nextPosition))
                    }

                    isChasing = true
                }
            }
            if (isChasing) break
        }

        return isChasing
    }

    /**
     * Returns a neighboring position that is closest to the target position.
     */
    private fun getGreedyPosition(chaserPos: Position3D, targetPos: Position3D): Position3D {
        val (targetX, targetY) = targetPos
        val (chaserX, chaserY) = chaserPos

        val greedyX = when {
            targetX > chaserX -> 1
            targetX < chaserX -> -1
            else -> 0
        }

        val greedyY = when {
            targetY > chaserY -> 1
            targetY < chaserY -> -1
            else -> 0
        }

        return chaserPos.withRelativeX(greedyX).withRelativeY(greedyY)
    }
}