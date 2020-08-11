package attributes

import block.GameBlock
import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Position3D

class AutoRunDetails : Attribute {

    var shouldRun = false
    var expectedBoringFloorNeighborCount = 0
    var initialDirection: Position3D = Position3D.unknown()
    val visited: MutableSet<Position3D> = mutableSetOf()

    fun isBoringFloor(block: GameBlock?): Boolean {
        // TODO: Refine this (e.g. don't ignore items)
        return block != null && !block.isObstructed
    }
}