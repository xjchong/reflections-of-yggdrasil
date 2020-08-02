package attributes

import org.hexworks.amethyst.api.Attribute


data class EquippableDetails(val type: EquippableType) : Attribute


sealed class EquippableType
object OneHanded : EquippableType()
object TwoHanded : EquippableType()
object Offhand : EquippableType()
object Neck : EquippableType()
object Finger : EquippableType()
object Wrists : EquippableType()
object Head : EquippableType()
object Arms : EquippableType()
object Body : EquippableType()
object Legs : EquippableType()
