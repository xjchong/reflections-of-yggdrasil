package events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class GameLogEvent(
        override val emitter: Any,
        val message: String,
        val type: GameLogEventType = Info) : Event {

    companion object {
        val KEY = GameLogEvent::class.simpleName

        fun log(message: String, type: GameLogEventType) {
            Zircon.eventBus.publish(GameLogEvent(Unit, message, type))
        }
    }
}