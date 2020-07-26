package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Position3D

class Vision(val radius: Int) : Attribute {
    val visiblePositions: MutableSet<Position3D> = mutableSetOf()
}