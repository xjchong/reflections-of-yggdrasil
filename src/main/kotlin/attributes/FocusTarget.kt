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
import org.hexworks.zircon.api.graphics.Symbols

class FocusTarget : DisplayableAttribute {

    private val targetProperty: Property<Maybe<AnyEntity>> = createPropertyFrom(Maybe.empty())
    var target: Maybe<AnyEntity> by targetProperty.asDelegate()
        private set

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

        val leftCapLabel = Components.label()
                .withSize(1, 1)
                .withText("${Symbols.SINGLE_LINE_T_DOUBLE_RIGHT}")

        val rightCapLabel = Components.label()
                .withSize(1, 1)
                .withText("${Symbols.SINGLE_LINE_T_DOUBLE_LEFT}")

        val healthBarLabel = CombatStats.getBarLabel(19,
                healthProperty, maxHealthProperty, GameColor.DARK_RED)

        val staminaBarLabel = CombatStats.getBarLabel(19,
                staminaProperty, maxStaminaProperty, GameColor.LIGHT_YELLOW)

        textBoxBuilder
                .addInlineComponent(nameLabel)
                .addInlineComponent(leftCapLabel.createCopy().build())
                .addInlineComponent(healthBarLabel)
                .addInlineComponent(rightCapLabel.createCopy().build())
                .addInlineComponent(leftCapLabel.createCopy().build())
                .addInlineComponent(staminaBarLabel)
                .addInlineComponent(rightCapLabel.createCopy().build())
                .commitInlineElements()

        targetProperty.onChange {
            it.newValue.optional?.getAttribute(CombatStats::class)?.let {combatStats ->
                healthProperty.updateFrom(combatStats.healthProperty)
                maxHealthProperty.updateFrom(combatStats.maxHealthProperty)
                staminaProperty.updateFrom(combatStats.staminaProperty)
                maxStaminaProperty.updateFrom(combatStats.maxStaminaProperty)
                refreshBars()
            }

            nameLabel.textProperty.value = it.newValue.optional?.name?.capitalize() ?: ""
        }

        return textBoxBuilder.build()
    }

    fun updateTarget(newTarget: AnyEntity) {
        targetProperty.updateValue(Maybe.of(newTarget))
    }

    fun clearTarget() {
        targetProperty.updateValue(Maybe.empty())
        healthProperty.updateValue(0)
        maxHealthProperty.updateValue(1)
        staminaProperty.updateValue(0)
        maxStaminaProperty.updateValue(1)
    }

    /**
     * Trigger a visual update of the bars. Not sure why this is necessary, but sadly it works.
     */
    private fun refreshBars() {
        val health = healthProperty.value
        val stamina = staminaProperty.value

        healthProperty.updateValue(health + 1)
        healthProperty.updateValue(health)

        staminaProperty.updateValue(stamina + 1)
        staminaProperty.updateValue(stamina)
    }
}