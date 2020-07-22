package facets.active

import builders.InventoryModalBuilder
import commands.InspectInventory
import commands.Wear
import commands.Wield
import entity.executeBlockingCommand
import entity.inventory
import entity.position
import events.DropInputEvent
import events.EatInputEvent
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
        return command.responseWhenCommandIs(InspectInventory::class) { (context, inventoryOwner) ->
            val (world, screen) = context
            val position = inventoryOwner.position

            val inventoryModal = InventoryModalBuilder(screen).build(inventoryOwner.inventory,
                    onDrop = { content ->
                        world.update(screen, DropInputEvent(content))
                    },
                    onEat = { consumable ->
                        world.update(screen, EatInputEvent(consumable))
                        inventoryOwner.inventory.remove(consumable)
                    },
                    onWield = { equipment ->
                        if (inventoryOwner.inventory.remove(equipment)) {
                            equipment.executeBlockingCommand(Wield(context, equipment, inventoryOwner))
                        }
                    },
                    onWear = { equipment ->
                        if (inventoryOwner.inventory.remove(equipment)) {
                            equipment.executeBlockingCommand(Wear(context, equipment, inventoryOwner))
                        }
                    })

            screen.openModal(inventoryModal)

            Consumed
        }
    }
}