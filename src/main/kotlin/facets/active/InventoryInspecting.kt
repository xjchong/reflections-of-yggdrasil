package facets.active

import builders.InventoryModalBuilder
import commands.InspectInventory
import entity.inventory
import events.DropInputEvent
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
        return command.responseWhenCommandIs(InspectInventory::class) { (context, inventoryOwner, position) ->
            val (world, screen) = context
            val inventoryModal = InventoryModalBuilder.build(screen, inventoryOwner.inventory) { item ->
                world.update(screen, DropInputEvent(item = item))
            }

            screen.openModal(inventoryModal)

            Consumed
        }
    }
}