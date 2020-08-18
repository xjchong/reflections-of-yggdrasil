package facets

import attributes.Equipments
import attributes.Inventory
import commands.Drop
import commands.InspectEquipments
import entity.getAttribute
import entity.position
import events.EquipmentsMenuEvent
import extensions.responseWhenIs
import game.GameContext
import kotlinx.coroutines.runBlocking
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object EquipmentsInspectable : BaseFacet<GameContext>(Equipments::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(InspectEquipments::class) { (context, equipmentsOwner) ->
            val equipments = equipmentsOwner.getAttribute(Equipments::class) ?: return@responseWhenIs Pass

            EquipmentsMenuEvent.publish(equipments, onUnequip = {
                val unequipped = equipments.unequip(it) ?: return@publish
                val inventory = equipmentsOwner.getAttribute(Inventory::class)

                context.world.observeSceneBy(equipmentsOwner, "The $equipmentsOwner unequips the $unequipped.")

                if (inventory == null || !inventory.add(unequipped)) {
                    runBlocking {
                        unequipped.executeCommand(Drop(context, unequipped, equipmentsOwner, equipmentsOwner.position))
                    }
                }
            })

            Consumed
        }
    }
}