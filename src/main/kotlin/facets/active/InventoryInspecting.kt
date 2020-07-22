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

            fun onEquip(equipment: Equipment) {
                inventoryOwner.whenTypeIs<EquipmentUserType> {
                    if (!inventoryOwner.inventory.remove(equipment)) return@whenTypeIs

                    equipments.equip(equipment)?.let { oldEquipment ->
                        // If owner couldn't place the old item in inventory, try to drop it instead.
                        if (!inventoryOwner.inventory.add(oldEquipment)) {
                            executeBlockingCommand(
                                    Drop(context, inventoryOwner, oldEquipment, inventoryOwner.position))
                        }
                    }

                    world.observeSceneBy(this, "The $this equips the $equipment.")

                    // Force the world to update, since equipping was done for 'free'.
                    world.update(screen, WaitInputEvent())
                }
            }

            val inventoryModal = InventoryModalBuilder(screen).build(inventoryOwner.inventory,
                    onDrop = { content ->
                        world.update(screen, DropInputEvent(content))
                    },
                    onEat = { consumable ->
                        world.update(screen, EatInputEvent(consumable))
                        inventoryOwner.inventory.remove(consumable)
                    },
                    onWield = { wieldable ->
                        onEquip(wieldable)
                    },
                    onWear = { wearable ->
                        onEquip(wearable)
                    })

            screen.openModal(inventoryModal)

            Consumed
        }
    }
}