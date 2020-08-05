package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.zircon.api.data.Position3D

class EntityPosition(initialPosition: Position3D = Position3D.unknown()) : Attribute {
    private val positionProperty = createPropertyFrom(initialPosition)

    var position: Position3D by positionProperty.asDelegate()
    var lastPosition = Position3D.unknown()

    init {
        positionProperty.onChange {
            lastPosition = it.oldValue
        }
    }
}
