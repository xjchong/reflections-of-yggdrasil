package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

class CombatStats(
    val maxHealthProperty: Property<Int>,
    val healthProperty: Property<Int> = createPropertyFrom(maxHealthProperty.value),
    val attackRatingProperty: Property<Int>,
    val defenseRatingProperty: Property<Int>
): Attribute {
    val maxHealth: Int by maxHealthProperty.asDelegate()
    var health: Int by healthProperty.asDelegate()
    val attackRating: Int by attackRatingProperty.asDelegate()
    val defenseRating: Int by attackRatingProperty.asDelegate()

    companion object {

        fun create(maxHealth: Int, health: Int = maxHealth, attackRating: Int, defenseRating: Int) =
            CombatStats(
                maxHealthProperty = createPropertyFrom(maxHealth),
                healthProperty =  createPropertyFrom(health),
                attackRatingProperty = createPropertyFrom(attackRating),
                defenseRatingProperty = createPropertyFrom(defenseRating)
            )
    }
}