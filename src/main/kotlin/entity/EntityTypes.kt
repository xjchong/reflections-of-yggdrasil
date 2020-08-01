package entity

import org.hexworks.amethyst.api.base.BaseEntityType

object NoType : BaseEntityType(
        name = "no type"
)

/**
 * CREATURE
 */

object Bat : BaseEntityType(
        name = "bat"
)

object Fungus : BaseEntityType(
        name = "fungus"
)

object Player : BaseEntityType(
        name = "player"
)

object Rat : BaseEntityType(
        name = "rat"
)

object Zombie : BaseEntityType(
        name = "zombie"
)

/**
 * CONSUMABLE
 */

object BatMeat : BaseEntityType(
        name = "bat meat",
        description = "Stringy bat meat. It is an acquired taste."
)

/**
 * TREASURE
 */

object En : BaseEntityType(
        name = "en"
)

/**
 * WIDGET
 */

object Door : BaseEntityType(
        name = "door"
)

object Wall : BaseEntityType(
        name = "wall"
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

object LeatherTunic : BaseEntityType(
        name = "leather tunic",
        description = "A tunic made of rugged leather. It is very comfortable."
)

object Chainmail : BaseEntityType(
        name = "chainmail",
        description = "A sturdy chainmail armor made of interlocking iron chains."
)

object Platemail : BaseEntityType(
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

