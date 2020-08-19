package facets

import attributes.EntityTime
import attributes.Equipments
import attributes.Inventory
import commands.Drop
import commands.Equip
import commands.Unequip
import entity.getAttribute
import entity.position
import entity.spendTime
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Equippable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
         return when (command) {
            is Equip -> command.executeEquip()
            is Unequip -> command.executeUnequip()
            else -> Pass
        }
    }

    private suspend fun Equip.executeEquip(): Response {
        val equipments = equipper.getAttribute(Equipments::class) ?: return Pass
        var response: Response = Pass
        val unequipped = equipments.equip(equippable)

        if (unequipped?.id == equippable.id) {
            context.world.observeSceneBy(equipper, "The $equipper can't wear the $equippable.")
        } else {
            equipper.spendTime(EntityTime.EQUIP)
            context.world.observeSceneBy(equipper, "The $equipper wears the $equippable.")
            response = Consumed
        }

        if (unequipped != null) {
            val inventory = equipper.getAttribute(Inventory::class)

            if (inventory == null || !inventory.add(unequipped)) {
                equippable.executeCommand(Drop(context, equipper, unequipped, equipper.position))
            }
        }

        return response
    }

    private suspend fun Unequip.executeUnequip(): Response {
        val equipments = equipper.getAttribute(Equipments::class) ?: return Pass
        val unequipped = equipments.unequip(equippable) ?: return Pass
        val inventory = equipper.getAttribute(Inventory::class)

        equipper.spendTime(EntityTime.UNEQUIP)
        context.world.observeSceneBy(equipper, "The $equipper unequips the $unequipped.")

        if (inventory == null || !inventory.add(unequipped)) {
            unequipped.executeCommand(Drop(context, unequipped, equipper, equipper.position))
        }

        return Consumed
    }
}