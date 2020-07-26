package attributes

import org.hexworks.amethyst.api.Attribute


data class Alliance(val faction: Faction) : Attribute


sealed class Faction

object Adventurer : Faction()
object Monster : Faction()