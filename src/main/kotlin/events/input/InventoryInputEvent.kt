package events.input

data class InventoryInputEvent(
        override val type: InputEventType = InputEventType.BACKGROUND
) : InputEvent