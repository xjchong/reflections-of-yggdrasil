package fragments.inventory

import attributes.Inventory
import entity.GameEntity
import entity.NoType
import fragments.TableFragmentDelegate
import fragments.TableRow

class InventoryTableDelegate(
    private val inventory: Inventory,
    private val onDrop: (GameEntity) -> Unit,
    private val onConsume: (GameEntity) -> Unit,
    private val onEquip: (GameEntity) -> Unit,
    private val onExamine: (GameEntity) -> Unit
) : TableFragmentDelegate() {

    init {
        inventory.currentHash.onChange {
            notifyDataChanged()
        }
    }

    override fun rowCount(): Int {
        return inventory.contents.size
    }

    override fun itemForRow(row: Int): TableRow {
        val inventoryItem = inventory.contents.getOrNull(row) ?: NoType.create()
        val rowFragment = InventoryRow(
            InventoryTable.width,
            inventoryItem
        )

        with(rowFragment) {
            dropButton.onActivated { onDrop(inventoryItem) }
            consumeButton.onActivated { onConsume(inventoryItem) }
            equipButton.onActivated { onEquip(inventoryItem) }
            onExamine = this@InventoryTableDelegate.onExamine
        }

        return rowFragment
    }
}