package attributes

import entity.AnyEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom

class Inventory(val size: Int) : Attribute {

    private val currentContents = mutableListOf<AnyEntity>()
    val currentHash = createPropertyFrom(currentContents.hashCode())

    val contents: List<AnyEntity>
        get() = currentContents.toList()

    val isEmpty: Boolean
        get() = currentContents.isEmpty()

    val isFull: Boolean
        get() = currentContents.size >= size

    fun findEntityBy(id: UUID): AnyEntity? {
        return currentContents.firstOrNull { it.id == id }
    }

    fun add(content: AnyEntity): Boolean {
        if (isFull.not() && currentContents.add(content)) {
            currentHash.updateValue(currentContents.hashCode())
            return true
        }

        return false
    }

    fun remove(content: AnyEntity): Boolean {
        if (currentContents.remove(content)) {
            currentHash.updateValue(currentContents.hashCode())
            return true
        }

        return false
    }
}