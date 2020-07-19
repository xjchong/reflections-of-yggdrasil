package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom


class EnergyLevel(initialEnergy: Int, val maxEnergy: Int) : Attribute {

    var currentEnergy: Int
        get() = currentValueProperty.value
        set(value) {
            currentValueProperty.value = value.coerceAtMost(maxEnergy)
        }

    private val currentValueProperty = createPropertyFrom(initialEnergy)
}