package behaviors

import attributes.Presence
import commands.AttemptAnyAction
import commands.AttemptAttack
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
        var enemy: AnyEntity? = null

        for (sensedPos in entity.sensedPositions.minus(entity.position)) {
            world.fetchBlockAt(sensedPos).ifPresent { block ->
                block.entities.firstOrNull { !entity.isAlliedWith(it) }?.let { enemy = it }
            }

            if (enemy != null) break
        }

        return enemy?.let { handleEnemy(context, entity, it)
        } ?: handleNoEnemy(context, entity)
    }

    private suspend fun handleEnemy(context: GameContext, attacker: AnyEntity, target: AnyEntity): Boolean {
        attacker.executeCommand(AttemptAttack(context, attacker, target)).let {
            if (it is Pass) {
                moveToEnemy(context, attacker, target)
            }
        }

        return true
    }

    private suspend fun moveToEnemy(context: GameContext, chaser: AnyEntity, target: AnyEntity): Boolean {
        val targetPresence = target.getAttribute(Presence::class)
        var nextPosition: Position3D = Position3D.unknown()

        // Attempt to get the next move using presence maps.
        if (targetPresence != null) {
            var lowestApproachVal: Int? = null

            for (neighbor in chaser.position.neighbors()) {
                val approachVal = targetPresence.approachMap[neighbor] ?: continue

                if (lowestApproachVal == null || approachVal < lowestApproachVal) {
                    nextPosition = neighbor
                    lowestApproachVal = approachVal
                }
            }
        }

        // If couldn't use presence maps to get next best move, then attempt an even simpler greedy approach.
        if (nextPosition == Position3D.unknown()) {
            nextPosition = getGreedyPosition(context, chaser.position, target.position)
        }

        if (chaser.executeCommand(AttemptAnyAction(context, chaser, nextPosition)) == Pass) {
            chaser.executeCommand(Move(context, chaser, nextPosition))
        }

        return true
    }

    /**
     * Returns a neighboring position that is closest to the target position.
     */
    private fun getGreedyPosition(context: GameContext, chaserPos: Position3D, targetPos: Position3D): Position3D {
        val world = context.world
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

        val greedyPos = chaserPos.withRelativeX(greedyX).withRelativeY(greedyY)
        var nextPosition = greedyPos

        if (greedyPos != targetPos && world.fetchBlockAt(greedyPos).optional?.isObstructed == true) {

            // The greedy position is blocked, and not by the target, so try other potential positions.
            val potentialMoves = chaserPos.neighbors().filter { potentialPos ->
                if (world.fetchBlockAt(potentialPos).optional?.isObstructed != false) {
                    return@filter false
                }

                when { // Get a position in the general direction of the target.
                    potentialPos == greedyPos -> false // This position was already determined to be blocked.
                    targetPos > chaserPos -> potentialPos > chaserPos
                    else -> potentialPos < chaserPos
                }
            }

            potentialMoves.firstOrNull()?.let { nextPosition = it }
        }

        return nextPosition
    }

    private suspend fun handleNoEnemy(context: GameContext, entity: AnyEntity): Boolean {
        return false
    }
}