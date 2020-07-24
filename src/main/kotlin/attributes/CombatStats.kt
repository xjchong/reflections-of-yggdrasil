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
import org.hexworks.zircon.api.graphics.Symbols


class CombatStats(
        val maxHealthProperty: Property<Int>,
        val healthProperty: Property<Int> = createPropertyFrom(maxHealthProperty.value),
        val attackRatingProperty: Property<Int>,
        val defenseRatingProperty: Property<Int>
) : DisplayableAttribute {
    val maxHealth: Int by maxHealthProperty.asDelegate()
    var health: Int by healthProperty.asDelegate()
    val attackRating: Int by attackRatingProperty.asDelegate()
    val defenseRating: Int by defenseRatingProperty.asDelegate()

    companion object {

        const val BAR_WIDTH = 24

        fun create(maxHealth: Int, health: Int = maxHealth, attackRating: Int, defenseRating: Int) =
                CombatStats(
                        maxHealthProperty = createPropertyFrom(maxHealth),
                        healthProperty = createPropertyFrom(health),
                        attackRatingProperty = createPropertyFrom(attackRating),
                        defenseRatingProperty = createPropertyFrom(defenseRating)
                )
    }

    override fun toComponent(width: Int): Component = Components.vbox()
            .withSize(width, 6)
            .build().apply {
                val textBoxBuilder = Components.textBox(width)

                val healthBarLabel = setupBar(textBoxBuilder, health, maxHealth, GameColor.GREEN)
                healthProperty.onChange { updateBar(healthBarLabel, health, maxHealth) }

                val hpLabel = Components.label().withSize(width, 1)
                        .build()

                addComponent(textBoxBuilder.build())
                addComponent(hpLabel)
            }

    private fun setupBar(textBoxBuilder: TextBoxBuilder, value: Int, maxValue: Int, color: TileColor): Label {
        val leftCapLabel = Components.label()
                .withSize(1, 1)
                .withText("${Symbols.SINGLE_LINE_T_DOUBLE_RIGHT}")
                .build()

        val rightCapLabel = Components.label()
                .withSize(1, 1)
                .withText("${Symbols.SINGLE_LINE_T_DOUBLE_LEFT}")
                .build()

        val barLabel = Components.label()
                .withSize(BAR_WIDTH, 1)
                .withText(getBarString(value, maxValue))
                .withStyle(color)
                .build()

        textBoxBuilder
                .addInlineComponent(leftCapLabel)
                .addInlineComponent(barLabel)
                .addInlineComponent(rightCapLabel)
                .commitInlineElements()
                .addNewLine()

        return barLabel
    }

    private fun updateBar(barLabel: Label, value: Int, maxValue: Int) {
        barLabel.textProperty.value = getBarString(value, maxValue)
    }

    private fun getBarString(value: Int, maxValue: Int): String {
        val increment = (maxValue.toDouble() / BAR_WIDTH.toDouble()) / 4.0
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