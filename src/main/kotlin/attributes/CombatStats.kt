package attributes

import GameColor
import extensions.withStyle
import org.hexworks.cobalt.databinding.api.binding.bindPlusWith
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.TextBoxBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed


class CombatStats(
        private val maxHealthProperty: Property<Int>,
        val healthProperty: Property<Int> = createPropertyFrom(maxHealthProperty.value),
        private val maxStaminaProperty: Property<Int>,
        val staminaProperty: Property<Int> = createPropertyFrom(maxStaminaProperty.value),
        private val powerProperty: Property<Double>,
        private val skillProperty: Property<Double>,
        private val luckProperty: Property<Double>
) : DisplayableAttribute {
    val maxHealth: Int by maxHealthProperty.asDelegate()
    val health: Int by healthProperty.asDelegate()
    val maxStamina: Int by maxStaminaProperty.asDelegate()
    val stamina: Int by staminaProperty.asDelegate()
    val power: Double by powerProperty.asDelegate()
    val skill: Double by skillProperty.asDelegate()
    val luck: Double by luckProperty.asDelegate()

    val attackRating: Double
        get() {
            val staminaBonus = if (stamina > 0) 1.0 else 0.5
            return (power + skill) * 100 * staminaBonus
        }

    companion object {

        fun create(maxHealth: Int, health: Int = maxHealth,
                   maxStamina: Int, stamina: Int = maxStamina,
                   power: Double = 0.0, skill: Double = 0.0, luck: Double = 0.0) =
                CombatStats(
                        maxHealthProperty = createPropertyFrom(maxHealth),
                        healthProperty = createPropertyFrom(health),
                        maxStaminaProperty = createPropertyFrom(maxStamina),
                        staminaProperty = createPropertyFrom(stamina),
                        powerProperty = createPropertyFrom(power),
                        skillProperty = createPropertyFrom(skill),
                        luckProperty = createPropertyFrom(luck)
                )

        fun getBarComponent(textBoxBuilder: TextBoxBuilder, width: Int,
                            valueProp: Property<Int>, maxValueProp: Property<Int>,
                            color: TileColor): Component {
            val barLabel = setupBar(textBoxBuilder, width, valueProp, maxValueProp, color)
            valueProp.onChange { updateBar(barLabel, valueProp.value, maxValueProp.value) }

            return barLabel
        }

        private fun setupBar(textBoxBuilder: TextBoxBuilder,
                             width: Int,
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

            val barLabelSize = Size.create(width - 3, 1)
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

            return barLabel
        }

        private fun updateBar(barLabel: Label, value: Int, maxValue: Int) {
            barLabel.textProperty.value = getBarString(barLabel.size.width, value, maxValue)
        }

        private fun getBarString(width: Int, value: Int, maxValue: Int): String {
            val increment = (maxValue.toDouble() / width.toDouble()) / 4.0
            var currentValue = value.toDouble()
            var barString = ""

            if (increment <= 0) {
                return barString // Just a sanity check that increment != 0, which could loop.
            }

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

    override fun toComponent(width: Int): Component = Components.vbox()
            .withSize(width, 5)
            .build().apply {
                val textBoxBuilder = Components.textBox(width)

                val healthBarLabel = getBarComponent(textBoxBuilder, textBoxBuilder.size.width,
                        healthProperty, maxHealthProperty, GameColor.DARK_GREEN)
                textBoxBuilder.commitInlineElements()

                val staminaBarLabel = getBarComponent(textBoxBuilder, textBoxBuilder.size.width,
                        staminaProperty, maxStaminaProperty, GameColor.LIGHT_YELLOW)
                textBoxBuilder.commitInlineElements()

                val statsLabel = Components.label()
                        .withDecorations(box(boxType = BoxType.TOP_BOTTOM_DOUBLE))
                        .withSize(width - 1, 3)
                        .withText("POW ${power.toString().padStart(2)} ${Symbols.SINGLE_LINE_VERTICAL} " +
                                "TEC ${skill.toString().padStart(2)} ${Symbols.SINGLE_LINE_VERTICAL} " +
                                "LUC ${luck.toString().padStart(2)}")
                        .build()

                statsLabel.textProperty.updateFrom(createPropertyFrom("POW ")
                        .bindPlusWith(createPropertyFrom((power * 100).toInt().toString().padStart(2)))
                        .bindPlusWith(createPropertyFrom(" ${Symbols.SINGLE_LINE_VERTICAL} TEC "))
                        .bindPlusWith(createPropertyFrom((skill * 100).toInt().toString().padStart(2)))
                        .bindPlusWith(createPropertyFrom(" ${Symbols.SINGLE_LINE_VERTICAL} LUC "))
                        .bindPlusWith(createPropertyFrom((luck * 100).toInt().toString().padStart(2))))

                addComponent(textBoxBuilder.build())
                addComponent(statsLabel)
            }

    fun regenStamina(amount: Int) {
        staminaProperty.value = (stamina + amount).coerceAtMost(maxStamina)
    }

    fun dockStamina(amount: Int) {
        staminaProperty.value = (stamina - amount).coerceAtLeast(0)
    }

    fun dockHealth(amount: Int) {
        healthProperty.value = (health - amount).coerceAtLeast(0)
    }
}