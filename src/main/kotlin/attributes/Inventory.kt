package attributes

import entity.AnyGameEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom

class Inventory(val size: Int) : Attribute {

    private val currentContents = mutableListOf<AnyGameEntity>()
    val currentHash = createPropertyFrom(currentContents.hashCode())

    val contents: List<AnyGameEntity>
        get() = currentContents.toList()

    val isEmpty: Boolean
        get() = currentContents.isEmpty()

    val isFull: Boolean
        get() = currentContents.size >= size

    fun findEntityBy(id: UUID): AnyGameEntity? {
        return currentContents.firstOrNull { it.id == id }
    }

    fun add(content: AnyGameEntity): Boolean {
        if (isFull.not() && currentContents.add(content)) {
            currentHash.updateValue(currentContents.hashCode())
            return true
        }

        return false
    }

    fun remove(content: AnyGameEntity): Boolean {
        if (currentContents.remove(content)) {
            currentHash.updateValue(currentContents.hashCode())
            return true
        }

        return false
    }
}