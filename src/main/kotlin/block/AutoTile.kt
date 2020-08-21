package block

import extensions.east
import extensions.north
import extensions.south
import extensions.west
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.GraphicalTile
import org.hexworks.zircon.api.data.Position3D

/**
 * An [AutoTile] determines which graphical tiles to display given an autotile context and a bitmapping strategy.
 * The reason there are lists for each configuration is to allow for animated tiles.
 *
 * Below are the possible configurations for tiles. Filled neighbours are those with the same [types] as this [AutoTile].
 * '#' represents
 *
 *  ... .#. ... ##.
 *  .0. .1. #2. #3.
 *  ... ... ... ...
 *
 *  ... .## ... ###
 *  .4# .5# #6# #7#
 *  ... ... ... ...
 *
 *  ... .#. ... ##.
 *  .8. .9. #A. #B.
 *  .#. .#. ##. ##.
 *
 *  ... .## ... ###
 *  .C# .D# #E# #F#
 *  .## .## ### ###
 *
 */
data class AutoTile(
        val types: List<EntityType>,
        val center: List<GraphicalTile>, // F
        val island: List<GraphicalTile> = center, // 0
        val southPoint: List<GraphicalTile> = center, // 1
        val eastPoint: List<GraphicalTile> = center, // 2
        val southEastCorner: List<GraphicalTile> = center, // 3
        val westPoint: List<GraphicalTile> = center, // 4
        val southWestCorner: List<GraphicalTile> = center, // 5
        val eastWestLine: List<GraphicalTile> = center, // 6
        val southEdge: List<GraphicalTile> = center, // 7
        val northPoint: List<GraphicalTile> = center, // 8
        val northSouthLine: List<GraphicalTile> = center, // 9
        val northEastCorner: List<GraphicalTile> = center, // A
        val eastEdge: List<GraphicalTile> = center, // B
        val northWestCorner: List<GraphicalTile> = center, // C
        val westEdge: List<GraphicalTile> = center, // D
        val northEdge: List<GraphicalTile> = center // E
) {

    fun graphics(context: AutoTileContext): List<GraphicalTile> {
        val (position, neighbors) = context
        var bitValue = 0

        if (neighbors[position.north]?.entities?.any { types.contains(it.type) } != false) {
            bitValue += 1
        }
        if (neighbors[position.west]?.entities?.any { types.contains(it.type) } != false) {
            bitValue += 2
        }
        if (neighbors[position.east]?.entities?.any { types.contains(it.type) } != false) {
            bitValue += 4
        }
        if (neighbors[position.south]?.entities?.any { types.contains(it.type) } != false) {
            bitValue += 8
        }

        return when (bitValue) {
            0 -> island
            1 -> southPoint
            2 -> eastPoint
            3 -> southEastCorner
            4 -> westPoint
            5 -> southWestCorner
            6 -> eastWestLine
            7 -> southEdge
            8 -> northPoint
            9 -> northSouthLine
            10 -> northEastCorner
            11 -> eastEdge
            12 -> northWestCorner
            13 -> westEdge
            14 -> northEdge
            15 -> center
            else -> listOf()
        }
    }
}

data class AutoTileContext(val position: Position3D, val neighbors: Map<Position3D, GameBlock?>)