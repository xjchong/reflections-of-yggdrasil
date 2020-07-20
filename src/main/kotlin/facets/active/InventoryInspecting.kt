package facets.active

import builders.InventoryModalBuilder
import commands.InspectInventory
import entity.*
import events.DropInputEvent
import events.EatInputEvent
import events.WaitInputEvent
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
            val inventoryModal = InventoryModalBuilder(screen).build(inventoryOwner.inventory,
                    onDrop = { item ->
                        world.update(screen, DropInputEvent(item))
                    },
                    onEat = { food ->
                        world.update(screen, EatInputEvent(food))
                        inventoryOwner.inventory.remove(food)
                    },
                    onEquip = { combatItem ->
                        var oldCombatItem: CombatItem? = null

                        inventoryOwner.whenTypeIs<EquipmentWearerType> {
                            inventoryOwner.inventory.remove(combatItem)
                            oldCombatItem = equipment.equip(combatItem)
                            world.observeSceneBy(this, "The $this equips the $combatItem.")
                            // Force the world to update, since equipping was done for 'free'.
                            world.update(screen, WaitInputEvent())
                        }

                        oldCombatItem
                    })

            screen.openModal(inventoryModal)

            Consumed
        }
    }
}