package fragments

import attributes.Inventory
import entity.AnyEntity
import entity.ConsumableType
import entity.GameEntity
import entity.whenTypeIs
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.AttachedComponent
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox

class InventoryFragment(
        private val inventory: Inventory,
        private val width: Int,
        private val onDrop: (AnyEntity) -> Unit,
        private val onEat: (GameEntity<ConsumableType>) -> Unit,
        private val onEquip: (AnyEntity) -> Unit
) : Fragment {

    private val attachedRows: MutableList<AttachedComponent> = mutableListOf()

    companion object {
        const val NAME_COLUMN_WIDTH = 15
        const val ACTIONS_COLUMN_WIDTH = 20
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

                    inventory.contents.forEach { item ->
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

        inventory.contents.forEach { item ->
            addInventoryRow(vBox, item)
        }
    }

    private fun addInventoryRow(vBox: VBox, entity: AnyEntity) {
        val inventoryRow = InventoryRowFragment(width, entity)
        val attachedInventoryRow = vBox.addFragment(inventoryRow)
        attachedRows.add(attachedInventoryRow)

        with (inventoryRow) {
            dropButton.onActivated { onDrop(entity) }
            eatButton.onActivated { entity.whenTypeIs<ConsumableType> { onEat(this) } }
            equipButton.onActivated { onEquip(entity) }
        }
    }
}