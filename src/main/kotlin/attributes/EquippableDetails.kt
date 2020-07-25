package attributes

import org.hexworks.amethyst.api.Attribute


data class EquippableDetails(
        val type: EquippableType,
        val reliability: Double,
        val efficiency: Double
) : Attribute {

    val attackModifier: Double
        get() {
            return if (Math.random() < reliability) efficiency else 1.0
        }

    val defenseModifier: Double
        get() {
            return if (Math.random() < reliability) efficiency else 0.0
        }
}


sealed class EquippableType
object OneHanded : EquippableType()
object TwoHanded : EquippableType()
object Offhand : EquippableType()
object Neck : EquippableType()
object Finger : EquippableType()
object Wrists : EquippableType()
object Head : EquippableType()
object Hands : EquippableType()
object Chest : EquippableType()
object Waist : EquippableType()
object Legs : EquippableType()
object Feet : EquippableType()
