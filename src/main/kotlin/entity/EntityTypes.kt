package entity

import org.hexworks.amethyst.api.base.BaseEntityType


object Bat : BaseEntityType(
        name = "bat"
), Combatant, InventoryOwnerType

object Door : BaseEntityType(
        name = "door"
)

object En : BaseEntityType(
        name = "en"
), ItemType

object Fungus : BaseEntityType(
        name = "fungus"
), Combatant

object Player : BaseEntityType(
        name = "player"
), Combatant, EnergyUserType, InventoryOwnerType

object Wall : BaseEntityType(
        name = "wall"
)