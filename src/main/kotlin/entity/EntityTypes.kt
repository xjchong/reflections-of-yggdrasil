package entity

import org.hexworks.amethyst.api.base.BaseEntityType


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