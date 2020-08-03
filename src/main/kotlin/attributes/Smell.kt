package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Position3D

class Smell(val radius: Int) : Attribute {
    val smellablePositions: MutableSet<Position3D> = mutableSetOf()
}