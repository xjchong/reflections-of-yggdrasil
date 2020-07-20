package fragments

import attributes.Inventory
import entity.*
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.AttachedComponent
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox

class InventoryFragment(
        private val inventory: Inventory,
        private val width: Int,
        private val onDrop: (Item) -> Unit,
        private val onEat: (Food) -> Unit,
        private val onEquip: (CombatItem) -> Unit) : Fragment {

    private val attachedRows: MutableList<AttachedComponent> = mutableListOf()

    companion object {
        const val NAME_COLUMN_WIDTH = 15
        const val ACTIONS_COLUMN_WIDTH = 10
    }

    override val root = Components.vbox()
            .withSize(width, inventory.size + 1)
            .build().apply {
                addComponent(Components.hbox()
                        .withSpacing(1)
                        .withSize(width, 1)
                        .build().apply {
                            addComponent(Components.label().withText("").withSize(1, 1))
                            addComponent(Components.header().withText("Name").withSize(NAME_COLUMN_WIDTH, 1))
                            addComponent(Components.header().withText("Actions").withSize(ACTIONS_COLUMN_WIDTH, 1))
                        })

                    inventory.items.forEach { item ->
                        addInventoryRow(this, item)
                    }

                    inventory.currentHash.onChange {
                        update(this)
                    }
                }

    private fun update(vBox: VBox) {
        while (attachedRows.isNotEmpty()) {
            attachedRows.removeAt(0).detach()
        }

        inventory.items.forEach { item ->
            addInventoryRow(vBox, item)
        }
    }

    private fun addInventoryRow(vBox: VBox, item: Item) {
        val inventoryRow = InventoryRowFragment(width, item)
        val attachedInventoryRow = vBox.addFragment(inventoryRow)
        attachedRows.add(attachedInventoryRow)

        inventoryRow.onDrop = {
            onDrop(item)
        }

        inventoryRow.onEat = {
            item.whenTypeIs<FoodType> {
                onEat(this)
            }
        }

        inventoryRow.onEquip = {
            item.whenTypeIs<CombatItemType> {
                onEquip(this)
            }
        }
    }
}