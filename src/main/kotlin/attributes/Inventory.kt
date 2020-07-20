package attributes

import entity.Item
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom

class Inventory(val size: Int) : Attribute {

    private val currentItems = mutableListOf<Item>()
    val currentHash = createPropertyFrom(currentItems.hashCode())

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
        if (isFull.not() && currentItems.add(item)) {
            currentHash.updateValue(currentItems.hashCode())
            return true
        }

        return false
    }

    fun remove(item: Item): Boolean {
        if (currentItems.remove(item)) {
            currentHash.updateValue(currentItems.hashCode())
            return true
        }

        return false
    }
}