package attributes

import org.hexworks.amethyst.api.Attribute


class FactionDetails(val ownFaction: Faction,
                     val alliedFactions: Set<Faction> = setOf(ownFaction),
                     val neutralFactions: Set<Faction> = setOf(Widget)) : Attribute


sealed class Faction

object Adventurer : Faction()
object Monster : Faction()
object Widget : Faction()