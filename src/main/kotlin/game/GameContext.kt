package game

import events.input.InputEvent
import events.input.InputEventType
import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.screen.Screen

data class GameContext(
        val world: World,
        val screen: Screen,
        val event: InputEvent) : Context {

    val isBackground = event.type == InputEventType.BACKGROUND
}
