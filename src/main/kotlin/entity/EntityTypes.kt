package entity

import org.hexworks.amethyst.api.base.BaseEntityType


object Bat : BaseEntityType(
        name = "bat"
), Combatant, InventoryOwnerType

object BatMeat : BaseEntityType(
        name = "bat meat",
        description = "Stringy bat meat. It is an acquired taste."
), ConsumableType

object Door : BaseEntityType(
        name = "door"
)

object En : BaseEntityType(
        name = "en"
)

object Fungus : BaseEntityType(
        name = "fungus"
), Combatant

object Player : BaseEntityType(
        name = "player"
), Combatant, EnergyUserType, InventoryOwnerType, EquipmentUserType

object Wall : BaseEntityType(
        name = "wall"
)

object Zombie : BaseEntityType(
        name = "zombie"
), Combatant, EquipmentUserType, InventoryOwnerType

/**
 * EQUIPMENT
 */

object Dagger : BaseEntityType(
        name = "rusty dagger",
        description = "A small, rusty dagger made of some metal alloy."
), WeaponType

object Sword : BaseEntityType(
        name = "iron sword",
        description = "A shiny sword made of iron. It is a two-hand weapon"
), WeaponType

object Staff : BaseEntityType(
        name = "wooden staff",
        description = "A wooden staff made of birch. It has seen some use"
), WeaponType

object LightArmor : BaseEntityType(
        name = "leather tunic",
        description = "A tunic made of rugged leather. It is very comfortable."
), ArmorType

object MediumArmor : BaseEntityType(
        name = "chainmail",
        description = "A sturdy chainmail armor made of interlocking iron chains."
), ArmorType

object HeavyArmor : BaseEntityType(
        name = "platemail",
        description = "A heavy and shiny platemail armor made of bronze."
), ArmorType

object Club : BaseEntityType(
        name = "club",
        description = "A wooden club. It doesn't give you an edge over your opponent (haha)."
), WeaponType

object Jacket : BaseEntityType(
        name = "jacket",
        description = "Dirty and rugged jacket made of leather."
), ArmorType

