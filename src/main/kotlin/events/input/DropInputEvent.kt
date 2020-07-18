package events.input

import entity.Item

data class DropInputEvent(
        override val type: InputEventType = InputEventType.FOREGROUND,
        val item: Item
) : InputEvent