package facets

import attributes.Equipments
import commands.InspectEquipments
import entity.getAttribute
import events.EquipmentsMenuEvent
import events.UnequipInputEvent
import extensions.responseWhenIs
import game.GameContext
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
                context.world.update(UnequipInputEvent(it))
            })

            Consumed
        }
    }
}