package attributes

import GameColor
import extensions.toStringProperty
import extensions.withStyle
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components

class CoinPouch(private val valueProperty: Property<Int>) : DisplayableAttribute {

    companion object {
        val MAX_CAPACITY = 999999
        val CURRENCY_NAME = "en"

        fun create(value: Int): CoinPouch = CoinPouch(valueProperty = createPropertyFrom(value))
    }

    val value: Int by valueProperty.asDelegate()

    override fun toComponent(width: Int) = Components.label()
            .withSize(width, 1)
            .withStyle(GameColor.CYAN)
            .build().apply {
                textProperty.updateFrom(valueProperty.toStringProperty() {
                    it.toString().padStart(width - CURRENCY_NAME.length) + CURRENCY_NAME
                })
            }

    fun addValue(amount: Int): Boolean {
        val nextValue = value + amount

        if (nextValue > MAX_CAPACITY) return false

        valueProperty.updateValue(nextValue)
        return true
    }

    fun removeValue(amount: Int): Boolean {
        val nextValue = value - amount

        if (nextValue < 0) return false

        valueProperty.updateValue(nextValue)
        return true
    }
}