package facets.active

import attributes.Equipment
import attributes.Inventory
import commands.Drop
import commands.Equip
import entity.*
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object EquipmentWearing : BaseFacet<GameContext>(Equipment::class, Inventory::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Equip::class) { (context, entity, item) ->
            val oldEquipment = entity.equipment.equip(item)

            oldEquipment?.let {
                entity.whenTypeIs<InventoryOwnerType> {
                    // Try to place the old equipment in the inventory.
                    if (!inventory.add(oldEquipment)) {
                        // Otherwise, try to drop it on the ground.
                        executeBlockingCommand(Drop(context, this, oldEquipment, position))
                    }
                }
            }

            Consumed
        }
    }
}