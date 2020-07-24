package attributes

import GameColor
import extensions.withStyle
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.TextBoxBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed


class CombatStats(
        val maxHealthProperty: Property<Int>,
        val healthProperty: Property<Int> = createPropertyFrom(maxHealthProperty.value),
        val maxStaminaProperty: Property<Int>,
        val staminaProperty: Property<Int> = createPropertyFrom(maxStaminaProperty.value)
) : DisplayableAttribute {
    val maxHealth: Int by maxHealthProperty.asDelegate()
    var health: Int by healthProperty.asDelegate()
    val maxStamina: Int by maxStaminaProperty.asDelegate()
    val stamina: Int by staminaProperty.asDelegate()

    companion object {

        fun create(maxHealth: Int, health: Int = maxHealth, maxStamina: Int, stamina: Int = maxStamina) =
                CombatStats(
                        maxHealthProperty = createPropertyFrom(maxHealth),
                        healthProperty = createPropertyFrom(health),
                        maxStaminaProperty = createPropertyFrom(maxStamina),
                        staminaProperty = createPropertyFrom(stamina)
                )
    }

    override fun toComponent(width: Int): Component = Components.vbox()
            .withSize(width, 3)
            .build().apply {
                val textBoxBuilder = Components.textBox(width)

                val healthBarLabel = setupBar(textBoxBuilder, healthProperty, maxHealthProperty, GameColor.DARK_GREEN)
                healthProperty.onChange { updateBar(healthBarLabel, health, maxHealth) }

                val staminaBarLabel = setupBar(textBoxBuilder, staminaProperty, maxStaminaProperty, GameColor.LIGHT_YELLOW)
                staminaProperty.onChange { updateBar(staminaBarLabel, stamina, maxStamina) }

                addComponent(textBoxBuilder.build())

            }

    private fun setupBar(textBoxBuilder: TextBoxBuilder,
                         valueProp: Property<Int>,
                         maxValueProp: Property<Int>,
                         color: TileColor): Label {
        val leftCapLabel = Components.label()
                .withSize(1, 1)
                .withText("${Symbols.SINGLE_LINE_T_DOUBLE_RIGHT}")
                .build()

        val rightCapLabel = Components.label()
                .withSize(1, 1)
                .withText("${Symbols.SINGLE_LINE_T_DOUBLE_LEFT}")
                .build()


        val barLabelSize = Size.create(textBoxBuilder.size.width - 3, 1)
        val barLabel = Components.label()
                .withSize(barLabelSize)
                .withText(getBarString(barLabelSize.width, valueProp.value, maxValueProp.value))
                .withStyle(color)
                .build()

        barLabel.handleMouseEvents(MouseEventType.MOUSE_ENTERED) { _, _ ->
            barLabel.textProperty.value = " ${valueProp.value}/${maxValueProp.value}"
            Processed
        }

        barLabel.handleMouseEvents(MouseEventType.MOUSE_EXITED) { _, _ ->
            updateBar(barLabel, valueProp.value, maxValueProp.value)
            Processed
        }

        textBoxBuilder
                .addInlineComponent(leftCapLabel)
                .addInlineComponent(barLabel)
                .addInlineComponent(rightCapLabel)
                .commitInlineElements()

        return barLabel
    }

    private fun updateBar(barLabel: Label, value: Int, maxValue: Int) {
        barLabel.textProperty.value = getBarString(barLabel.size.width, value, maxValue)
    }

    private fun getBarString(width: Int, value: Int, maxValue: Int): String {
        val increment = (maxValue.toDouble() / width.toDouble()) / 4.0
        var currentValue = value.toDouble()
        var barString = ""

        while (currentValue > 0) {
            if (currentValue - (increment * 4) > -increment) {
                barString += "${Symbols.BLOCK_SOLID}"
                currentValue -= (increment * 4)
            } else if (currentValue - (increment * 3) > -increment) {
                barString += "${Symbols.BLOCK_DENSE}"
                currentValue -= (increment * 3)
            } else if (currentValue - (increment * 2) > -increment) {
                barString += "${Symbols.BLOCK_MIDDLE}"
                currentValue -= (increment * 2)
            } else if (currentValue - increment > -increment) {
                barString += "${Symbols.BLOCK_SPARSE}"
                currentValue -= increment
            } else {
                break
            }
        }

        return barString
    }
}