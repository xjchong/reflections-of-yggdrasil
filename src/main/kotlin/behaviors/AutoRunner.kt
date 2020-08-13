package behaviors

import attributes.AutoRunDetails
import attributes.Senses
import commands.Move
import entity.AnyEntity
import entity.getAttribute
import entity.isEnemiesWith
import entity.position
import events.AutoRunInputEvent
import extensions.adjacentNeighbors
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.Pass
import org.hexworks.zircon.api.data.Position3D


object AutoRunner : ForegroundBehavior(AutoRunDetails::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val event = context.event
        if (event !is AutoRunInputEvent) return false

        val senses = entity.getAttribute(Senses::class)
        val details = entity.getAttribute(AutoRunDetails::class) ?: return event.cancel()

        // Check if missing any needed attributes.
        if (!details.shouldRun) return event.cancel()
        if (senses != null && senses.sensedEntities.any { entity.isEnemiesWith(it) }) {
            return event.cancel()
        }

        // Check if the neighbors are boring (if they aren't, then stop auto running.
        val boringFloorNeighbors = entity.position.adjacentNeighbors(false).filter {
            details.isBoringFloor(context.world.fetchBlockAt(it).optional)
        }
        if (boringFloorNeighbors.size != details.expectedBoringFloorNeighborCount) return event.cancel()

        // Get the next possible moves, if any.
        details.visited.add(entity.position)
        val nextPossibleMoves = boringFloorNeighbors.filter {
            !details.visited.contains(it)
        }
        if (nextPossibleMoves.isEmpty()) return event.cancel()

        // Execute a move in the correct direction.
        if (nextPossibleMoves.size == 1) {
            if (Move(context, entity) { nextPossibleMoves.first() }.execute() == Pass) {
                return event.cancel()
            }
        } else if (nextPossibleMoves.size > 1 && details.initialDirection != Position3D.unknown()) {
            if (Move(context, entity) { entity.position.withRelative(details.initialDirection) }.execute() == Pass) {
                return event.cancel()
            }
        } else { // Multiple possible moves, but our initial direction is unknown.
            return event.cancel()
        }

        return true
    }

    private fun AutoRunInputEvent.cancel(): Boolean {
        onInterrupt()

        return false
    }
}