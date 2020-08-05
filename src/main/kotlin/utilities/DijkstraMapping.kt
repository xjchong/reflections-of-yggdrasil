package utilities

import extensions.neighbors
import org.hexworks.zircon.api.data.Position3D


object DijkstraMapping {

    fun getAvoidanceMap(avoidedPositions: Set<Position3D>, maxRange: Int, isBlocked: (Position3D) -> Boolean): HashMap<Position3D, Int> {
        val approachMap = hashSetOf<Position3D>()
        val avoidanceMap = hashMapOf<Position3D, Int>()
        val goalPositions = mutableSetOf<Position3D>()

        // First get a map as if we wanted to approach the avoided positions.
        fun findNextApproach(candidates: Set<Position3D>, remainingRange: Int) {
            if (remainingRange == 0) return
            val nextCandidates = mutableSetOf<Position3D>()

            for (candidate in candidates) {
                if (approachMap.contains(candidate) || isBlocked(candidate)) continue

                approachMap.add(candidate)
                nextCandidates.addAll(candidate.neighbors(false))

                // When we are at the max range for our map, add these fringe positions as where we want to go.
                if (remainingRange == 1) {
                    goalPositions.add(candidate)
                }
            }

            findNextApproach(nextCandidates, remainingRange - 1)
        }

        findNextApproach(avoidedPositions, maxRange)

        // Now, using the fringe positions we found, create a similar map with values leading (decreasing) towards them.
        fun findNextAvoidance(candidates: Set<Position3D>, value: Int) {
            if (candidates.isEmpty()) return

            val nextCandidates = mutableSetOf<Position3D>()

            for (candidate in candidates) {
                if (!approachMap.contains(candidate)) continue
                if (avoidanceMap.containsKey(candidate)) continue

                avoidanceMap[candidate] = value
                nextCandidates.addAll(candidate.neighbors(false))
            }

            findNextAvoidance(nextCandidates, value + 1)
        }

        findNextAvoidance(goalPositions, 0)

        return avoidanceMap
    }
}