package extension

import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D

/**
 * Creates a collection of [Position3D]s in the order in which they should
 * be iterated when drawing from one 3D position to another (imagine a rectangular slice of cake).
 */
fun Size3D.fetchPositionsForSlice(startPos: Position3D, width: Int, height: Int, depth: Int): Sequence<Position3D> {
    return sequence {
        (0 until depth).flatMap { z ->
            (0 until height).flatMap { y ->
                (0 until width).map { x ->
                    yield(startPos.withRelative(Position3D.create(x, y, z)))
                }
            }
        }
    }
}
