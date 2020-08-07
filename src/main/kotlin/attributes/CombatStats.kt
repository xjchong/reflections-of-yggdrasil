package attributes

import GameColor
import extensions.withStyle
import org.hexworks.cobalt.databinding.api.binding.bindPlusWith
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed


class CombatStats(
        val maxHealthProperty: Property<Int>,
        val healthProperty: Property<Int> = createPropertyFrom(maxHealthProperty.value),
        val maxStaminaProperty: Property<Int>,
        val staminaProperty: Property<Int> = createPropertyFrom(maxStaminaProperty.value),
        private val powerProperty: Property<Double>,
        private val techProperty: Property<Double>,
        private val luckProperty: Property<Double>
) : DisplayableAttribute {

    val maxHealth: Int by maxHealthProperty.asDelegate()
    val health: Int by healthProperty.asDelegate()
    val maxStamina: Int by maxStaminaProperty.asDelegate()
    val stamina: Int by staminaProperty.asDelegate()
    val power: Double by powerProperty.asDelegate()
    val tech: Double by techProperty.asDelegate()
    val luck: Double by luckProperty.asDelegate()

    companion object {

        fun create(maxHealth: Int, health: Int = maxHealth,
                   maxStamina: Int, stamina: Int = maxStamina,
                   power: Double = 0.0, tech: Double = 0.0, luck: Double = 0.0) =
                CombatStats(
                        maxHealthProperty = createPropertyFrom(maxHealth),
                        healthProperty = createPropertyFrom(health),
                        maxStaminaProperty = createPropertyFrom(maxStamina),
                        staminaProperty = createPropertyFrom(stamina),
                        powerProperty = createPropertyFrom(power),
                        techProperty = createPropertyFrom(tech),
                        luckProperty = createPropertyFrom(luck)
                )
    }

    override fun toComponent(width: Int): Component = Components.vbox()
            .withSize(width, 5)
            .build().apply {
                val textBoxBuilder = Components.textBox(width)

                val leftCapLabel = Components.label()
                        .withSize(1, 1)
                        .withText("${Symbols.SINGLE_LINE_T_DOUBLE_RIGHT}")

                val rightCapLabel = Components.label()
                        .withSize(1, 1)
                        .withText("${Symbols.SINGLE_LINE_T_DOUBLE_LEFT}")

                val healthBarLabel = getBarLabel(width - 2,
                        healthProperty, maxHealthProperty, GameColor.DARK_GREEN)

                val staminaBarLabel = getBarLabel(width - 2,
                        staminaProperty, maxStaminaProperty, GameColor.LIGHT_YELLOW)

                textBoxBuilder
                        .addInlineComponent(leftCapLabel.createCopy().build())
                        .addInlineComponent(healthBarLabel)
                        .addInlineComponent(rightCapLabel.createCopy().build())
                        .commitInlineElements()
                        .addInlineComponent(leftCapLabel.createCopy().build())
                        .addInlineComponent(staminaBarLabel)
                        .addInlineComponent(rightCapLabel.createCopy().build())
                        .commitInlineElements()

                val statsPanel = Components.panel()
                        .withSize(width - 1, 3)
                        .withDecorations(box(boxType = BoxType.TOP_BOTTOM_DOUBLE))
                        .build().apply {
                            val statsLabel = Components.label()
                                    .withStyle(GameColor.GREY)
                                    .withSize(width - 3, 1)
                                    .withText("POW ${power.toString().padStart(2)} ${Symbols.SINGLE_LINE_VERTICAL} " +
                                            "TEC ${tech.toString().padStart(2)} ${Symbols.SINGLE_LINE_VERTICAL} " +
                                            "LUC ${luck.toString().padStart(2)}")
                                    .build()

                            statsLabel.textProperty.updateFrom(createPropertyFrom("POW ")
                                    .bindPlusWith(createPropertyFrom((power * 100).toInt().toString().padStart(2)))
                                    .bindPlusWith(createPropertyFrom(" ${Symbols.SINGLE_LINE_VERTICAL} TEC "))
                                    .bindPlusWith(createPropertyFrom((tech * 100).toInt().toString().padStart(2)))
                                    .bindPlusWith(createPropertyFrom(" ${Symbols.SINGLE_LINE_VERTICAL} LUC "))
                                    .bindPlusWith(createPropertyFrom((luck * 100).toInt().toString().padStart(2))))

                            addComponent(statsLabel)
                        }

                addComponent(textBoxBuilder.build())
                addComponent(statsPanel)
            }

    fun getHealthBarLabel(width: Int, color: TileColor, isCompact: Boolean = false): Label {
        return getBarLabel(width, healthProperty, maxHealthProperty, color, isCompact)
    }

    fun getStaminaBarLabel(width: Int, color: TileColor, isCompact: Boolean = false): Label {
        return getBarLabel(width, staminaProperty, maxStaminaProperty, color, isCompact)
    }

    fun gainStamina(amount: Int) {
        staminaProperty.value = (stamina + amount).coerceAtMost(maxStamina)
    }

    fun dockStamina(amount: Int): Boolean {
        if (stamina == 0) return false

        staminaProperty.value = (stamina - amount).coerceAtLeast(0)

        return true
    }

    fun gainHealth(amount: Int) {
        healthProperty.value = (health + amount).coerceAtMost(maxHealth)
    }

    fun dockHealth(amount: Int) {
        healthProperty.value = (health - amount).coerceAtLeast(0)
    }

    private fun getBarLabel(width: Int, valueProp: Property<Int>, maxValueProp: Property<Int>, color: TileColor,
                            isCompact: Boolean = false): Label {
        val barLabel = setupBar(width, valueProp, maxValueProp, color, isCompact)

        valueProp.onChange {
            updateBar(barLabel, valueProp.value, maxValueProp.value)
        }

        return barLabel
    }

    private fun setupBar(width: Int, valueProp: Property<Int>, maxValueProp: Property<Int>, color: TileColor,
                         isCompact: Boolean = false): Label {
        val barLabelSize = Size.create(width - 1, 1)
        val barLabel = Components.label()
                .withSize(barLabelSize)
                .withText(getBarString(barLabelSize.width, valueProp.value, maxValueProp.value))
                .withStyle(color, TileColor.transparent())
                .build()

        barLabel.handleMouseEvents(MouseEventType.MOUSE_ENTERED) { _, _ ->
            barLabel.textProperty.value = if (isCompact) {
                " ${valueProp.value}"
            } else {
                " ${valueProp.value}/${maxValueProp.value}"
            }
            Processed
        }

        barLabel.handleMouseEvents(MouseEventType.MOUSE_EXITED) { _, _ ->
            updateBar(barLabel, valueProp.value, maxValueProp.value)
            Processed
        }

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