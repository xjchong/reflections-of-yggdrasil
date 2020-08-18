package attributes

import entity.GameEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

class Inventory(val size: Int) : Attribute {

    private val currentContents = mutableListOf<GameEntity>()
    val dataTimestamp: Property<Long> = createPropertyFrom(System.currentTimeMillis())

    val contents: List<GameEntity>
        get() = currentContents.toList()

    val isEmpty: Boolean
        get() = currentContents.isEmpty()

    val isFull: Boolean
        get() = currentContents.size >= size

    fun findEntityBy(id: UUID): GameEntity? {
        return currentContents.firstOrNull { it.id == id }
    }

    fun add(content: GameEntity): Boolean {
        if (isFull.not() && currentContents.add(content)) {
            updateDataTimestamp()
            return true
        }

        return false
    }

    fun remove(content: GameEntity): Boolean {
        if (currentContents.remove(content)) {
            updateDataTimestamp()
            return true
        }

        return false
    }

    private fun updateDataTimestamp() {
        dataTimestamp.updateValue(System.currentTimeMillis())
    }
}