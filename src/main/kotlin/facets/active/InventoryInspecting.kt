package facets.active

import attributes.Inventory
import commands.InspectInventory
import entity.getAttribute
import entity.position
import events.ConsumeInputEvent
import events.DropInputEvent
import events.EquipInputEvent
import events.InventoryMenuEvent
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object InventoryInspecting : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(InspectInventory::class) { (context, entity) ->
            val (world, screen) = context
            val position = entity.position

            entity.getAttribute(Inventory::class)?.let { inventory ->
                InventoryMenuEvent.publish(
                        inventory,
                        onDrop = { content ->
                            world.update(screen, DropInputEvent(content))
                        },
                        onConsume = { consumable ->
                            world.update(screen, ConsumeInputEvent(consumable))
                            inventory.remove(consumable)
                        },
                        onEquip = { equipment ->
                            if (inventory.remove(equipment)) {
                                world.update(screen, EquipInputEvent(equipment))
                            }
                        })
            }

            Consumed
        }
    }
}