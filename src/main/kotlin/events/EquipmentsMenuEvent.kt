package events

import attributes.Equipments
import entity.GameEntity
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class EquipmentsMenuEvent(
    val equipments: Equipments,
    val onUnequip: suspend (GameEntity) -> Unit
) : Event {

    companion object {
        const val KEY = "EquipmentsMenuEvent"

        fun publish(equipments: Equipments, onUnequip: suspend (GameEntity) -> Unit) {
            Zircon.eventBus.publish(EquipmentsMenuEvent(equipments, onUnequip))
        }
    }

    override val emitter: Any = this
}