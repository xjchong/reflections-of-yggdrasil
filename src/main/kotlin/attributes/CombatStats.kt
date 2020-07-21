package attributes

import extensions.toStringProperty
import org.hexworks.cobalt.databinding.api.binding.bindPlusWith
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

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

        fun create(maxHealth: Int, health: Int = maxHealth, attackRating: Int, defenseRating: Int) =
                CombatStats(
                        maxHealthProperty = createPropertyFrom(maxHealth),
                        healthProperty = createPropertyFrom(health),
                        attackRatingProperty = createPropertyFrom(attackRating),
                        defenseRatingProperty = createPropertyFrom(defenseRating)
                )
    }

    override fun toComponent(width: Int): Component = Components.vbox()
            .withSize(width, 5)
            .build().apply {
                val healthLabel = Components.label()
                        .withSize(width, 1)
                        .build()

                val attackRatingLabel = Components.label()
                        .withSize(width, 1)
                        .build()

                val defenseRatingLabel = Components.label()
                        .withSize(width, 1)
                        .build()

                healthLabel.textProperty.updateFrom(
                        createPropertyFrom("HP:  ")
                                .bindPlusWith(healthProperty.toStringProperty())
                                .bindPlusWith(createPropertyFrom("/"))
                                .bindPlusWith(maxHealthProperty.toStringProperty()))

                attackRatingLabel.textProperty.updateFrom(
                        createPropertyFrom("ATK: ")
                                .bindPlusWith(attackRatingProperty.toStringProperty()))

                defenseRatingLabel.textProperty.updateFrom(
                        createPropertyFrom("DEF: ")
                                .bindPlusWith(defenseRatingProperty.toStringProperty()))

                addComponent(Components.textBox(width).addHeader("Combat Stats"))
                addComponent(healthLabel)
                addComponent(attackRatingLabel)
                addComponent(defenseRatingLabel)
            }
}