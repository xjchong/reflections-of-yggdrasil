package game

import events.Background
import events.GameInputEvent
import org.hexworks.amethyst.api.Context

data class GameContext(
        val world: World,
        val event: GameInputEvent) : Context {

    val inBackground = event.type == Background
}
