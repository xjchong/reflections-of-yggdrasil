package attributes

import GameColor
import entity.AnyEntity
import entity.getAttribute
import extensions.optional
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

class FocusTarget : DisplayableAttribute {

    var targetProperty: Property<Maybe<AnyEntity>> = createPropertyFrom(Maybe.empty())
    private val healthProperty: Property<Int> = createPropertyFrom(0)
    private val maxHealthProperty: Property<Int> = createPropertyFrom(1)

    override fun toComponent(width: Int): Component {
        val textBoxBuilder = Components.textBox(width - 2)
        val nameLabel = Components.label()
                .withText("")
                .withSize(16, 1)
                .build()

        textBoxBuilder.addInlineComponent(nameLabel)

        val healthBarLabel = CombatStats.getBarComponent(textBoxBuilder, 20,
                healthProperty, maxHealthProperty, GameColor.DARK_GREEN)

        targetProperty.onChange {
            val combatStatsCopy = it.newValue?.optional?.getAttribute(CombatStats::class)

            nameLabel.textProperty.value = it.newValue.optional?.name?.capitalize() ?: ""
            healthProperty.updateValue(combatStatsCopy?.health ?: 0)
            maxHealthProperty.updateValue(combatStatsCopy?.maxHealth ?: 1)

            it.newValue.ifPresent { target ->
                target.getAttribute(CombatStats::class)?.healthProperty?.onChange {
                    healthProperty.updateValue(it.newValue)
                    if (it.newValue <= 0) {
                        targetProperty.updateValue(Maybe.empty())
                    }
                }
            }
        }

        return textBoxBuilder.commitInlineElements().build()
    }
}