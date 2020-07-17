package entity

import org.hexworks.amethyst.api.base.BaseEntityType


object Bat : BaseEntityType(
    name = "bat"
), Combatant

object Door : BaseEntityType(
    name = "door"
)

object Fungus : BaseEntityType(
    name = "fungus"
), Combatant

object Player : BaseEntityType(
    name = "player"
), Combatant

object Wall : BaseEntityType(
    name = "wall"
)