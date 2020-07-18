package events.input

data class TakeInputEvent(
        override val type: InputEventType = InputEventType.FOREGROUND
) : InputEvent