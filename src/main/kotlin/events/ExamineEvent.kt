package events

import entity.AnyEntity
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class ExamineEvent(
        val entity: AnyEntity,
        val callback: () -> Unit = {}
) : Event {

    companion object {
        val KEY = "ExamineEvent"

        fun publish(entity: AnyEntity, callback: () -> Unit = {}) {
            Zircon.eventBus.publish(ExamineEvent(entity, callback))
        }
    }

    override val emitter: Any = this
}