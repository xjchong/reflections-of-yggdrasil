package events.input


interface InputEvent {

    val type: InputEventType
}


enum class InputEventType {
    FOREGROUND, BACKGROUND
}
