package facets

import attributes.Inventory
import commands.InspectInventory
import entity.getAttribute
import events.ConsumeInputEvent
import events.DropInputEvent
import events.EquipInputEvent
import events.InventoryMenuEvent
import extensions.responseWhenIs
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object InventoryInspectable : BaseFacet<GameContext>(Inventory::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(InspectInventory::class) { (context, inventoryOwner) ->
            val inventory = inventoryOwner.getAttribute(Inventory::class) ?: return@responseWhenIs Pass
            val world = context.world

            InventoryMenuEvent.publish(
                inventory,
                onDrop = { content ->
                    world.update(DropInputEvent(content))
                },
                onConsume = { consumable ->
                    world.update(ConsumeInputEvent(consumable))
                    inventory.remove(consumable)
                },
                onEquip = { equipment ->
                    if (inventory.remove(equipment)) {
                        world.update(EquipInputEvent(equipment))
                    }
                })

            Consumed
        }
    }
}