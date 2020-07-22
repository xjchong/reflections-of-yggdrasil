package facets.passive

import attributes.Equipments
import attributes.Inventory
import commands.Drop
import commands.Wear
import entity.executeBlockingCommand
import entity.getAttribute
import entity.position
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Wearable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Wear::class) { (context, equipper, equipment) ->
            equipper.findAttribute(Equipments::class).ifPresent { equipments ->
                context.world.observeSceneBy(equipper, "The $equipper wears the $equipment.")
                equipments.wear(equipment)?.let { unequipped ->
                    val inventory = equipper.getAttribute(Inventory::class)

                    if (inventory == null || !inventory.add(unequipped)) {
                        equipper.executeBlockingCommand(Drop(context, equipper, unequipped, equipper.position))
                    }
                }
            }

            Consumed
        }
    }
}
