package attributes

import extensions.toStringProperty
import org.hexworks.cobalt.databinding.api.binding.bindPlusWith
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component


class EnergyLevel(initialEnergy: Int, val maxEnergy: Int) : DisplayableAttribute {

    var currentEnergy: Int
        get() = currentValueProperty.value
        set(value) {
            currentValueProperty.value = value.coerceAtMost(maxEnergy)
        }

    private val currentValueProperty = createPropertyFrom(initialEnergy)

    override fun toComponent(width: Int): Component = Components.vbox()
            .withSize(width, 5)
            .build().apply {
                val hungerLabel = Components.label()
                        .withSize(width, 1)
                        .build()

                hungerLabel.textProperty.updateFrom(
                        currentValueProperty.toStringProperty()
                                .bindPlusWith(createPropertyFrom("/$maxEnergy")))

                addComponent(Components.textBox(width).addHeader("Hunger"))
                addComponent(hungerLabel)
            }
}