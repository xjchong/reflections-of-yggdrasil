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
    private val staminaProperty: Property<Int> = createPropertyFrom(0)
    private val maxStaminaProperty: Property<Int> = createPropertyFrom(1)

    override fun toComponent(width: Int): Component {
        val textBoxBuilder = Components.textBox(width - 2)
        val nameLabel = Components.header()
                .withText("")
                .withSize(16, 1)
                .build()

        textBoxBuilder.addInlineComponent(nameLabel)

        val healthBarLabel = CombatStats.getBarComponent(textBoxBuilder, 21,
                healthProperty, maxHealthProperty, GameColor.DARK_GREEN)

        val staminaBarLabel = CombatStats.getBarComponent(textBoxBuilder, 21,
                staminaProperty, maxStaminaProperty, GameColor.LIGHT_YELLOW)

        targetProperty.onChange {
            it.newValue.optional?.getAttribute(CombatStats::class)?.let {combatStats ->
                healthProperty.updateFrom(combatStats.healthProperty)
                maxHealthProperty.updateFrom(combatStats.maxHealthProperty)
                staminaProperty.updateFrom(combatStats.staminaProperty)
                maxStaminaProperty.updateFrom(combatStats.maxStaminaProperty)
            }

            nameLabel.textProperty.value = it.newValue.optional?.name?.capitalize() ?: ""

            it.newValue.ifPresent { target ->
                target.getAttribute(CombatStats::class)?.healthProperty?.onChange {
                    if (it.newValue <= 0) {
                        targetProperty.updateValue(Maybe.empty())
                        healthProperty.updateValue(0)
                        maxHealthProperty.updateValue(1)
                        staminaProperty.updateValue(0)
                        maxStaminaProperty.updateValue(1)
                    }
                }
            }
        }

        return textBoxBuilder.commitInlineElements().build()
    }
}