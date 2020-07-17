package util

import org.hexworks.zircon.api.data.Position
import kotlin.math.ceil
import kotlin.math.floor


/**
 * A [Quadrant] represents a 90 degree sector pointing north, south, east, or west.
 * [Quadrant]s are traversed row by row. For the east and west [Quadrant]s, these
 * "rows" are vertical, not horizontal.
 */
class Quadrant(private val cardinal: Int, private val origin: Position) {

    companion object {
        const val NORTH = 0
        const val EAST = 1
        const val SOUTH = 2
        const val WEST = 3
    }

    /**
     * Convert a [Position] relative to the current quadrant to a [Position]
     * representing an absolute position in the grid.
     */
    fun transform(position: Position): Position {
        val (row, col) = position
        return when (cardinal) {
            NORTH -> origin.withRelativeX(col).withRelativeY(-row)
            SOUTH -> origin.withRelativeX(col).withRelativeY(row)
            EAST -> origin.withRelativeX(row).withRelativeY(col)
            else -> origin.withRelativeX(-row).withRelativeY(col)
        }
    }
}

/**
 * A [Row] represents a segment of [Position]s bound between a start and end slope.
 * [depth] represents the distance between the row and the [Quadrant]'s origin.
 */
class Row(val depth: Int, var startSlope: Double, var endSlope: Double) {

    /**
     * Returns an iterator over the tiles in the row
     */
    fun positions(): Iterator<Position> {
        val minCol = roundTiesUp(depth * startSlope)
        val maxCol = roundTiesDown(depth * endSlope)

        return (minCol.toInt()..maxCol.toInt()).map {
            Position.create(depth, it)
        }.iterator()
    }

    fun next(): Row {
        return Row(depth + 1, startSlope, endSlope)
    }

    private fun roundTiesUp(number: Double): Double {
        return floor(number + 0.5)
    }

    private fun roundTiesDown(number: Double): Double {
        return ceil(number - 0.5)
    }
}

/**
 * Shadow casting FOV implementation translated from
 * [Albert Ford's python version](https://www.albertford.com/shadowcasting).
 */
object ShadowCasting {

    /**
     * Within [computeFOV], we define some local functions that abstract away the details of [Quadrant]s
     * from the `scan` function.
     */
    fun computeFOV(origin: Position, radius: Int, isBlocking: (Position) -> Boolean, markVisible: (Position) -> Unit) {
        markVisible(origin)

        repeat(4) { i ->
            val quadrant = Quadrant(i, origin)

            fun reveal(position: Position) {
                markVisible(quadrant.transform(position))
            }

            fun isOpaque(position: Position?): Boolean {
                return if (position != null) {
                    isBlocking(quadrant.transform(position))
                } else false
            }

            fun isOpen(position: Position?): Boolean {
                return if (position != null) {
                    isBlocking(quadrant.transform(position)).not()
                } else false
            }

            fun scan(row: Row) {
                if (row.depth >= radius) return

                var previousPosition: Position? = null

                for (position in row.positions()) {
                    if (isOpaque(position) || isSymmetric(row, position)) {
                        reveal(position)
                    }

                    if (isOpaque(previousPosition) && isOpen(position)) {
                        row.startSlope = getSlope(position)
                    }

                    if (isOpen(previousPosition) && isOpaque(position)) {
                        val nextRow = row.next()
                        nextRow.endSlope = getSlope(position)
                        scan(nextRow)
                    }

                    previousPosition = position
                }

                if (isOpen(previousPosition)) {
                    scan(row.next())
                }
            }

            val firstRow = Row(1, -1.0, 1.0)
            scan(firstRow)
        }
    }

    private fun isSymmetric(row: Row, position: Position): Boolean {
        val (_, x) = position

        return (x >= row.depth * row.startSlope
                && x <= row.depth * row.endSlope)
    }

    private fun getSlope(position: Position): Double {
        val rowDepth = position.x.toDouble()
        val col = position.y.toDouble()
        val dividend = (2 * col - 1)
        val divisor = (2 * rowDepth)

        return dividend / divisor
    }
}

