package extensions

import org.hexworks.zircon.api.data.Position3D

fun Position3D.neighbors(shouldShuffle: Boolean = true): List<Position3D> {
    val neighborPositions = (-1..1).flatMap { x ->
        (-1..1).map { y ->
            this.withRelativeX(x).withRelativeY(y)
        }
    }.minus(this)

    return if (shouldShuffle) neighborPositions.shuffled() else neighborPositions
}

fun Position3D.adjacentNeighbors(shouldShuffle: Boolean = true): List<Position3D> {
    val neighbors: List<Position3D> = listOf(
            this.withRelativeX(1),
            this.withRelativeY(1),
            this.withRelativeX(-1),
            this.withRelativeY(-1))

    return if (shouldShuffle) neighbors.shuffled() else neighbors
}
