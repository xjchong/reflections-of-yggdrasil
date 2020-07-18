package events.input

import org.hexworks.zircon.api.data.Position3D


data class MoveInputEvent(
        override val type: InputEventType = InputEventType.FOREGROUND,
        val relativePosition: Position3D
) : InputEvent