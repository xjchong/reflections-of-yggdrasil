package game

import events.Background
import events.GameInputEvent
import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.screen.Screen

data class GameContext(
        val world: World,
        val screen: Screen,
        val event: GameInputEvent) : Context {

    val inBackground = event.type == Background
}
