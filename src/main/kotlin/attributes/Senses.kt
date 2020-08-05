package attributes

import entity.AnyEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Position3D
import kotlin.math.max

class Senses(var vision: Int = 0, var smell: Int = 0) : Attribute {
    val visiblePositions: MutableSet<Position3D> = mutableSetOf()
    val smellablePositions: MutableSet<Position3D> = mutableSetOf()
    val sensedPositions: Set<Position3D>
        get() {
            return visiblePositions + smellablePositions
        }
    val sensedEntities: MutableSet<AnyEntity> = mutableSetOf()

    val maxRange: Int
        get() = max(vision, smell)
}