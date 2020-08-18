package events

import attributes.Inventory
import entity.GameEntity
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.internal.Zircon


data class InventoryMenuEvent(
    val inventory: Inventory,
    val onDrop: (GameEntity) -> Unit,
    val onConsume: (GameEntity) -> Unit,
    val onEquip: (GameEntity) -> Unit
) : Event {

    companion object {
        const val KEY = "InventoryMenuEvent"

        fun publish(inventory: Inventory, onDrop: (GameEntity) -> Unit,
                    onConsume: (GameEntity) -> Unit, onEquip: (GameEntity) -> Unit) {
           Zircon.eventBus.publish(InventoryMenuEvent(
                   inventory, onDrop, onConsume, onEquip
           ))
        }
    }

    override val emitter: Any = this
}