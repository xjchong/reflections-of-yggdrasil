package attributes

import entity.GameEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Position3D
import kotlin.math.max

class Senses(var vision: Int = 0, var smell: Int = 0) : Attribute {
    var visiblePositions: Set<Position3D> = setOf()
    var smellablePositions: Set<Position3D> = setOf()
    val sensedPositions: Set<Position3D>
        get() {
            return visiblePositions + smellablePositions
        }
    var sensedEntities: List<GameEntity> = listOf()

    val maxRange: Int
        get() = max(vision, smell)
}