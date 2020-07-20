package facets.active

import builders.InventoryModalBuilder
import commands.Drop
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
                        inventoryOwner.whenTypeIs<EquipmentWearerType> {
                            if (!inventoryOwner.inventory.remove(combatItem)) return@whenTypeIs

                            equipment.equip(combatItem)?.let {oldCombatItem ->
                                // If owner couldn't place the old item in inventory, try to drop it instead.
                                if (!inventoryOwner.inventory.add(oldCombatItem)) {
                                    executeBlockingCommand(
                                            Drop(context, inventoryOwner, oldCombatItem, inventoryOwner.position))
                                }
                            }

                            world.observeSceneBy(this, "The $this equips the $combatItem.")

                            // Force the world to update, since equipping was done for 'free'.
                            world.update(screen, WaitInputEvent())
                        }
                    })

            screen.openModal(inventoryModal)

            Consumed
        }
    }
}