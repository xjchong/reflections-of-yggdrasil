package events

import attributes.Inventory
import entity.AnyEntity
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class InventoryMenuEvent(
        val inventory: Inventory,
        val onExamine: (AnyEntity) -> Unit,
        val onDrop: (AnyEntity) -> Unit,
        val onConsume: (AnyEntity) -> Unit,
        val onEquip: (AnyEntity) -> Unit
) : Event {

    companion object {
        val KEY = "InventoryMenuEvent"

        fun publish(inventory: Inventory, onExamine: (AnyEntity) -> Unit, onDrop: (AnyEntity) -> Unit,
                 onConsume: (AnyEntity) -> Unit, onEquip: (AnyEntity) -> Unit) {
           Zircon.eventBus.publish(InventoryMenuEvent(
                   inventory, onExamine, onDrop, onConsume, onEquip
           ))
        }
    }

    override val emitter: Any
        get() = this
}