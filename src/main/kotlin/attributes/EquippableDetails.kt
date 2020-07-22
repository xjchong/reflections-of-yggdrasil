package attributes

import org.hexworks.amethyst.api.Attribute


data class EquippableDetails(
        val type: EquippableType
) : Attribute


sealed class EquippableType
object OneHanded : EquippableType()
object TwoHanded : EquippableType()
object Neck : EquippableType()
object Finger : EquippableType()
object Wrists : EquippableType()
object Head : EquippableType()
object Hands : EquippableType()
object Chest : EquippableType()
object Waist : EquippableType()
object Legs : EquippableType()
object Feet : EquippableType()
