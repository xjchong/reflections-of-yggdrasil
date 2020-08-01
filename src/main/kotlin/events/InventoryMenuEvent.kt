package events

import attributes.Inventory
import entity.AnyEntity
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class InventoryMenuEvent(
        val inventory: Inventory,
        val onDrop: (AnyEntity) -> Unit,
        val onConsume: (AnyEntity) -> Unit,
        val onEquip: (AnyEntity) -> Unit
) : Event {

    companion object {
        val KEY = "InventoryMenuEvent"

        fun publish(inventory: Inventory, onDrop: (AnyEntity) -> Unit,
                 onConsume: (AnyEntity) -> Unit, onEquip: (AnyEntity) -> Unit) {
           Zircon.eventBus.publish(InventoryMenuEvent(
                   inventory, onDrop, onConsume, onEquip
           ))
        }
    }

    override val emitter: Any
        get() = this
}