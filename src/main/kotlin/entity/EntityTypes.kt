package entity

import org.hexworks.amethyst.api.base.BaseEntityType


object Bat : BaseEntityType(
        name = "bat"
), Combatant, InventoryOwnerType

object BatMeat : BaseEntityType(
        name = "bat meat",
        description = "Stringy bat meat. It is an acquired taste."
), FoodType

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

/**
 * EQUIPMENT
 */

object Dagger : BaseEntityType(
        name = "Rusty Dagger",
        description = "A small, rusty dagger made of some metal alloy."
), WeaponType

object Sword : BaseEntityType(
        name = "Iron Sword",
        description = "A shiny sword made of iron. It is a two-hand weapon"
), WeaponType

object Staff : BaseEntityType(
        name = "Wooden Staff",
        description = "A wooden staff made of birch. It has seen some use"
), WeaponType

object LightArmor : BaseEntityType(
        name = "Leather Tunic",
        description = "A tunic made of rugged leather. It is very comfortable."
), ArmorType

object MediumArmor : BaseEntityType(
        name = "Chainmail",
        description = "A sturdy chainmail armor made of interlocking iron chains."
), ArmorType

object HeavyArmor : BaseEntityType(
        name = "Platemail",
        description = "A heavy and shiny platemail armor made of bronze."
), ArmorType

object Club : BaseEntityType(
        name = "Club",
        description = "A wooden club. It doesn't give you an edge over your opponent (haha)."
), WeaponType

object Jacket : BaseEntityType(
        name = "Leather jacket",
        description = "Dirty and rugged jacket made of leather."
), ArmorType

