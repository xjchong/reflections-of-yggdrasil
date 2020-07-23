package entity

import org.hexworks.amethyst.api.base.BaseEntityType


object Bat : BaseEntityType(
        name = "bat"
)

object BatMeat : BaseEntityType(
        name = "bat meat",
        description = "Stringy bat meat. It is an acquired taste."
)

object Door : BaseEntityType(
        name = "door"
)

object En : BaseEntityType(
        name = "en"
)

object Fungus : BaseEntityType(
        name = "fungus"
)

object Player : BaseEntityType(
        name = "player"
), EnergyUserType

object Wall : BaseEntityType(
        name = "wall"
)

object Zombie : BaseEntityType(
        name = "zombie"
)

/**
 * EQUIPMENT
 */

object Dagger : BaseEntityType(
        name = "rusty dagger",
        description = "A small, rusty dagger made of some metal alloy."
)

object Sword : BaseEntityType(
        name = "iron sword",
        description = "A shiny sword made of iron. It is a two-hand weapon"
)

object Staff : BaseEntityType(
        name = "wooden staff",
        description = "A wooden staff made of birch. It has seen some use"
)

object LightArmor : BaseEntityType(
        name = "leather tunic",
        description = "A tunic made of rugged leather. It is very comfortable."
)

object MediumArmor : BaseEntityType(
        name = "chainmail",
        description = "A sturdy chainmail armor made of interlocking iron chains."
)

object HeavyArmor : BaseEntityType(
        name = "platemail",
        description = "A heavy and shiny platemail armor made of bronze."
)

object Club : BaseEntityType(
        name = "club",
        description = "A wooden club. It doesn't give you an edge over your opponent (haha)."
)

object Jacket : BaseEntityType(
        name = "jacket",
        description = "Dirty and rugged jacket made of leather."
)

