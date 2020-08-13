package events

import entity.GameEntity
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class ExamineEvent(
    val entity: GameEntity,
    val callback: () -> Unit = {}
) : Event {

    companion object {
        val KEY = "ExamineEvent"

        fun publish(entity: GameEntity, callback: () -> Unit = {}) {
            Zircon.eventBus.publish(ExamineEvent(entity, callback))
        }
    }

    override val emitter: Any = this
}