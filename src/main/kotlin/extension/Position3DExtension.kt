package extension

import org.hexworks.zircon.api.data.Position3D

fun Position3D.neighbours(shouldShuffle: Boolean = true): List<Position3D> {
    val neighborPositions = (-1..1).flatMap { x ->
        (-1..1).map { y ->
            this.withRelativeX(x).withRelativeY(y)
        }
    }.minus(this)

    return if (shouldShuffle) neighborPositions.shuffled() else neighborPositions
}