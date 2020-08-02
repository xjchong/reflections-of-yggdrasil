package entity.factories

import attributes.*
import builders.AnyEntityBuilder
import constants.GameTile
import entity.*
import facets.passive.Droppable
import facets.passive.Equippable
import facets.passive.Takeable
import models.Bash
import models.Cut
import models.Resistance
import models.Stab
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.CharacterTile
import kotlin.random.Random

object ArmorFactory {

    const val VERY_UNRELIABLE = 0.2
    const val UNRELIABLE = 0.35
    const val OCCASIONAL = 0.5
    const val RELIABLE = 0.65
    const val VERY_RELIABLE = 0.8
    const val CONSTANT = 1.0

    const val HUGE_WEAKNESS = 1.8
    const val LARGE_WEAKNESS = 1.65
    const val MODERATE_WEAKNESS = 1.5
    const val SMALL_WEAKNESS = 1.35
    const val TINY_WEAKNESS = 1.2
    const val TINY_RESISTANCE = 0.8
    const val SMALL_RESISTANCE = 0.65
    const val MODERATE_RESISTANCE = 0.5
    const val LARGE_RESISTANCE = 0.35
    const val HUGE_RESISTANCE = 0.2
    const val IMMUNITY = 0.0

    fun newRandomArmor(): AnyEntity {
        return when (Random.nextInt(3)) {
            0 -> newLeatherTunic()
            1 -> newChainmail()
            else -> newPlatemail()
        }
    }

    // HEAD

    fun newSimpleCap() = newArmorBuilder(Head, SimpleCap, GameTile.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, RELIABLE, TINY_RESISTANCE)))
            .build()

    fun newFancyCap() = newArmorBuilder(Head, FancyCap, GameTile.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newAncientCap() = newArmorBuilder(Head, AncientCap, GameTile.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleHelm() = newArmorBuilder(Head, SimpleHelm, GameTile.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancyHelm() = newArmorBuilder(Head, FancyHelm, GameTile.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientHelm() = newArmorBuilder(Head, AncientHelm, GameTile.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, RELIABLE, LARGE_RESISTANCE),
                            Resistance(Bash, RELIABLE, LARGE_RESISTANCE)))
            .build()

    fun newSimpleSallet() = newArmorBuilder(Head, SimpleSallet, GameTile.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancySallet() = newArmorBuilder(Head, FancySallet, GameTile.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientSallet() = newArmorBuilder(Head, AncientSallet, GameTile.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, HUGE_RESISTANCE),
                            Resistance(Stab, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, RELIABLE, LARGE_RESISTANCE)))
            .build()

    // BODY

    fun newSimpleJacket() = newArmorBuilder(Chest, SimpleJacket, GameTile.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, TINY_RESISTANCE)))
            .build()

    fun newFancyJacket() = newArmorBuilder(Chest, FancyJacket, GameTile.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newAncientJacket() = newArmorBuilder(Chest, AncientJacket, GameTile.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleHauberk() = newArmorBuilder(Chest, SimpleHauberk, GameTile.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancyHauberk() = newArmorBuilder(Chest, FancyHauberk, GameTile.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientHauberk() = newArmorBuilder(Chest, AncientHauberk, GameTile.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, LARGE_RESISTANCE)))
            .build()

    fun newSimpleCuirass() = newArmorBuilder(Chest, SimpleCuirass, GameTile.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancyCuirass() = newArmorBuilder(Chest, FancyCuirass, GameTile.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientCuirass() = newArmorBuilder(Chest, AncientCuirass, GameTile.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, HUGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, LARGE_RESISTANCE)))
            .build()

    // HANDS

    fun newSimpleGloves() = newArmorBuilder(Hands, SimpleGloves, GameTile.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, TINY_RESISTANCE)))
            .build()

    fun newFancyGloves() = newArmorBuilder(Hands, FancyGloves, GameTile.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newAncientGloves() = newArmorBuilder(Hands, AncientGloves, GameTile.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleBracers() = newArmorBuilder(Hands, SimpleBracers, GameTile.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyBracers() = newArmorBuilder(Hands, FancyBracers, GameTile.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientBracers() = newArmorBuilder(Hands, AncientBracers, GameTile.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    fun newSimpleGauntlets() = newArmorBuilder(Hands, SimpleGauntlets, GameTile.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyGauntlets() = newArmorBuilder(Hands, FancyGauntlets, GameTile.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientGauntlets() = newArmorBuilder(Hands, AncientGauntlets, GameTile.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, HUGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    // LEGS

    fun newSimpleShoes() = newArmorBuilder(Legs, SimpleShoes, GameTile.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, TINY_RESISTANCE)))
            .build()

    fun newFancyShoes() = newArmorBuilder(Legs, FancyShoes, GameTile.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newAncientShoes() = newArmorBuilder(Legs, AncientShoes, GameTile.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleBoots() = newArmorBuilder(Legs, SimpleBoots, GameTile.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyBoots() = newArmorBuilder(Legs, FancyBoots, GameTile.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientBoots() = newArmorBuilder(Legs, AncientBoots, GameTile.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    fun newSimpleGreaves() = newArmorBuilder(Legs, SimpleGreaves, GameTile.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyGreaves() = newArmorBuilder(Legs, FancyGreaves, GameTile.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientGreaves() = newArmorBuilder(Legs, AncientGreaves, GameTile.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, HUGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    /**
     * TO BE DEPRECATED
     */

    fun newLeatherTunic() = newArmorBuilder(Chest, LeatherTunic, GameTile.LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newChainmail() = newArmorBuilder(Chest, Chainmail, GameTile.MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newPlatemail() = newArmorBuilder(Chest, Platemail, GameTile.HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newJacket() = newArmorBuilder(Chest, Jacket, GameTile.JACKET)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_UNRELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, VERY_UNRELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_UNRELIABLE, TINY_RESISTANCE)))
            .build()

    /**
     * HELPERS
     */

    private fun newArmorBuilder(equipType: EquippableType, type: EntityType, tile: CharacterTile) = AnyEntityBuilder.newBuilder(type)
            .withAttributes(
                    EntityTile(tile),
                    EntityPosition(),
                    EquippableDetails(equipType))
            .withFacets(Droppable, Takeable, Equippable)
}