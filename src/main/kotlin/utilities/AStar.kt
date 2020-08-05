package utilities

import extensions.neighbors
import org.hexworks.zircon.api.data.Position3D
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.max


object AStar {

    fun getPath(start: Position3D, goal: Position3D, getCost: (Position3D, Position3D) -> Double): Iterable<Position3D> {
        val frontier = PriorityQueue(FrontierEntry.comparator)
        val cameFrom = hashMapOf<Position3D, Position3D?>()
        val goesTo = hashMapOf<Position3D, Position3D?>()
        val costSoFar = hashMapOf<Position3D, Double>()
        var currentPosition: Position3D = goal

        frontier.add(FrontierEntry(goal, 0.0))
        cameFrom[goal] = null
        costSoFar[goal] = 0.0

        while (frontier.isNotEmpty()) {
            currentPosition = frontier.remove().position

            if (currentPosition == start) break

            for (nextPosition in currentPosition.neighbors(false).filter {
                costSoFar[it] == null
            }) {
                val currentCost = costSoFar[currentPosition] ?: continue
                val nextCost = getCost(currentPosition, nextPosition)
                val newCost = currentCost + nextCost + getHeuristic(nextPosition, start, nextCost)

                if (!costSoFar.contains(nextPosition) || newCost < currentCost) {
                    costSoFar[nextPosition] = newCost
                    frontier.add(FrontierEntry(nextPosition, newCost))
                    cameFrom[nextPosition] = currentPosition
                }
            }
        }

        val path = mutableListOf<Position3D>()

        while (currentPosition != goal) {
            path.add(currentPosition)
            currentPosition = cameFrom[currentPosition] ?: goal
        }
        path.add(goal)

        return path
    }

    private fun getHeuristic(from: Position3D, to: Position3D, cost: Double): Double {
        return max((from.x - to.x).absoluteValue, (from.y - to.y).absoluteValue).toDouble() * cost
    }
}

data class FrontierEntry(val position: Position3D, val priority: Double) {

    companion object {
        val comparator: Comparator<FrontierEntry> = compareBy { it.priority }
    }
}