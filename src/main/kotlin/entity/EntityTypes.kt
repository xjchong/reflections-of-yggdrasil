package entity

import builders.newGameEntityOfType
import org.hexworks.amethyst.api.base.BaseEntityType


object NoType : BaseEntityType(
    name = "no type"
) {
    fun create(): GameEntity {
        return newGameEntityOfType(this, {})
    }
}

/**
 * CREATURE
 */

object Bat : BaseEntityType(
    name = "bat"
)

object Crab : BaseEntityType(
    name = "crab"
)

object Goblin : BaseEntityType(
    name = "goblin"
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

object SmallCoins : BaseEntityType(
    name = "small pile of coins"
)

/**
 * WIDGET
 */

object Door : BaseEntityType(
    name = "door"
)

object Grass : BaseEntityType(
    name = "grass"
)

object Pot : BaseEntityType(
    name = "pot"
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

object Club : BaseEntityType(
    name = "club",
    description = "A wooden club. It doesn't give you an edge over your opponent (haha)."
)


/**
 * ARMOR
 */

// HEAD

object SimpleCap : BaseEntityType(
    name = "simple cap"
)

object FancyCap : BaseEntityType(
    name = "fancy cap"
)

object AncientCap : BaseEntityType(
    name = "ancient cap"
)

object SimpleHelm : BaseEntityType(
    name = "simple helm"
)

object FancyHelm : BaseEntityType(
    name = "fancy helm"
)

object AncientHelm : BaseEntityType(
    name = "ancient helm"
)

object SimpleSallet : BaseEntityType(
    name = "simple sallet"
)

object FancySallet : BaseEntityType(
    name = "fancy sallet"
)

object AncientSallet : BaseEntityType(
    name = "ancient sallet"
)

// BODY

object SimpleJacket : BaseEntityType(
    name = "simple jacket"
)

object FancyJacket : BaseEntityType(
    name = "fancy jacket"
)

object AncientJacket : BaseEntityType(
    name = "ancient jacket"
)

object SimpleHauberk : BaseEntityType(
    name = "simple hauberk"
)

object FancyHauberk : BaseEntityType(
    name = "fancy hauberk"
)

object AncientHauberk : BaseEntityType(
    name = "ancient hauberk"
)

object SimpleCuirass : BaseEntityType(
    name = "simple cuirass"
)

object FancyCuirass : BaseEntityType(
    name = "fancy cuirass"
)

object AncientCuirass : BaseEntityType(
    name = "ancient cuirass"
)

// ARMS

object SimpleGloves : BaseEntityType(
    name = "simple gloves"
)

object FancyGloves : BaseEntityType(
    name = "fancy gloves"
)

object AncientGloves : BaseEntityType(
    name = "ancient gloves"
)

object SimpleBracers : BaseEntityType(
    name = "simple bracers"
)

object FancyBracers : BaseEntityType(
    name = "fancy bracers"
)

object AncientBracers : BaseEntityType(
    name = "ancient bracers"
)

object SimpleGauntlets : BaseEntityType(
    name = "simple gauntlets"
)

object FancyGauntlets : BaseEntityType(
    name = "fancy gauntlets"
)

object AncientGauntlets : BaseEntityType(
    name = "ancient gauntlets"
)

// LEGS

object SimpleShoes : BaseEntityType(
    name = "simple shoes"
)

object FancyShoes : BaseEntityType(
    name = "fancy shoes"
)

object AncientShoes : BaseEntityType(
    name = "ancient shoes"
)

object SimpleBoots : BaseEntityType(
    name = "simple boots"
)

object FancyBoots : BaseEntityType(
    name = "fancy boots"
)

object AncientBoots : BaseEntityType(
    name = "ancient boots"
)

object SimpleGreaves : BaseEntityType(
    name = "simple greaves"
)

object FancyGreaves : BaseEntityType(
    name = "fancy greaves"
)

object AncientGreaves : BaseEntityType(
    name = "ancient greaves"
)
