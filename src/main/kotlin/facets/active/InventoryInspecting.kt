package facets.active

import attributes.Inventory
import builders.InventoryModalBuilder
import commands.InspectInventory
import entity.getAttribute
import entity.position
import events.DropInputEvent
import events.EatInputEvent
import events.EquipInputEvent
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Size


object InventoryInspecting : BaseFacet<GameContext>() {

    private val DIALOG_SIZE = Size.create(34, 15)

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(InspectInventory::class) { (context, entity) ->
            val (world, screen) = context
            val position = entity.position

            entity.getAttribute(Inventory::class)?.let { inventory ->
                val inventoryModal = InventoryModalBuilder(screen).build(inventory,
                        onDrop = { content ->
                            world.update(screen, DropInputEvent(content))
                        },
                        onEat = { consumable ->
                            world.update(screen, EatInputEvent(consumable))
                            inventory.remove(consumable)
                        },
                        onEquip = { equipment ->
                            if (inventory.remove(equipment)) {
                                world.update(screen, EquipInputEvent(equipment))
                            }
                        })

                screen.openModal(inventoryModal)
            }

            Consumed
        }
    }
}