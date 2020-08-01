package events

import entity.AnyEntity
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class ExamineEvent(
        val entity: AnyEntity
) : Event {

    companion object {
        val KEY = "ExamineEvent"

        fun publish(entity: AnyEntity) {
            Zircon.eventBus.publish(ExamineEvent(entity))
        }
    }

    override val emitter: Any = this
}