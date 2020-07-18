package attributes

import entity.Item
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.core.api.UUID

class Inventory(val size: Int) : Attribute {

    private val currentItems = mutableListOf<Item>()

    val items: List<Item>
        get() = currentItems.toList()

    val isEmpty: Boolean
        get() = currentItems.isEmpty()

    val isFull: Boolean
        get() = currentItems.size >= size

    fun findEntityBy(id: UUID): Item? {
        return currentItems.firstOrNull { it.id == id }
    }

    fun add(item: Item): Boolean {
        return if (isFull.not()) {
            currentItems.add(item)
        } else false
    }

    fun remove(item: Item): Boolean {
        return currentItems.remove(item)
    }
}