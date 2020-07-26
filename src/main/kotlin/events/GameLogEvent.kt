package events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class GameLogEvent(
        val message: String,
        val type: GameLogEventType = Info) : Event {

    companion object {
        val KEY = "GameLogEvent"

        fun log(message: String, type: GameLogEventType) {
            Zircon.eventBus.publish(GameLogEvent(message, type))
        }
    }

    override val emitter: Any
        get() = this
}