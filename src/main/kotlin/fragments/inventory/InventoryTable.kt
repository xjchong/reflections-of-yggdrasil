package fragments.inventory

import builders.InventoryModalBuilder
import fragments.TableFragment


class InventoryTable(height: Int) : TableFragment(width, height) {

    companion object {
        val width = InventoryModalBuilder.DIALOG_SIZE.width - 3
    }
}