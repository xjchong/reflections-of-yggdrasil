package views.fragments

import attributes.Inventory
import entity.Item
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.uievent.Processed

class InventoryFragment(
        inventory: Inventory,
        width: Int,
        onDrop: (Item) -> Unit) : Fragment {

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
                    val inventoryRow = InventoryRowFragment(width, item)
                    val attachedInventoryRow = addFragment(inventoryRow)

                    inventoryRow.dropButton.onActivated {
                        attachedInventoryRow.detach()
                        onDrop(item)
                        Processed
                    }
                }
            }
}