package events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon

data class GameLogEvent(override val emitter: Any, val message: String) : Event

fun logGameEvent(message: String) {
    Zircon.eventBus.publish(GameLogEvent(Unit, message))
}