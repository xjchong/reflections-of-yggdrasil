package facets.passive

import attributes.EntityTime
import attributes.Equipments
import attributes.Inventory
import commands.Drop
import commands.Equip
import entity.getAttribute
import entity.position
import entity.spendTime
import extensions.responseWhenIs
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Equippable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Equip::class) { (context, equipment, equipper) ->
            val equipments = equipper.getAttribute(Equipments::class) ?: return@responseWhenIs Pass
            var response: Response = Pass

            equipments.equip(equipment)?.let { unequipped ->
                if (unequipped.id == equipment.id) {
                    context.world.observeSceneBy(equipper, "The $equipper can't wear the $equipment.")
                } else {
                    equipper.spendTime(EntityTime.EQUIP)
                    context.world.observeSceneBy(equipper, "The $equipper wears the $equipment.")
                    response = Consumed
                }

                val inventory = equipper.getAttribute(Inventory::class)

                if (inventory == null || !inventory.add(unequipped)) {
                    response = equipper.executeCommand(Drop(context, equipper, unequipped, equipper.position))
                }
            }

            response
        }
    }
}