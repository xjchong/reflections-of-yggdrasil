package attributes

import game.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.zircon.api.data.Position3D

class EntityPosition(initialPosition: Position3D = Position3D.unknown()) : Attribute {
    private val positionProperty = createPropertyFrom(initialPosition)

    var position: Position3D by positionProperty.asDelegate()
        private set

    var lastPosition = Position3D.unknown()
        private set

    var lastUpdated: Long = 0
        private set

    fun updatePosition(newPosition: Position3D, turn: Long) {
        lastUpdated = turn
        lastPosition = position
        position = newPosition
    }

    /**
     * The number of turns since the last time this position updated.
     */
    fun staleness(context: GameContext): Long {
        return context.world.turn - lastUpdated
    }
}
