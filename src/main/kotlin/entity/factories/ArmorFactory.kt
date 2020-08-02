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
import utilities.WeightedCollection
import utilities.WeightedEntry

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
        val weightedCollection: WeightedCollection<() -> AnyEntity> = WeightedCollection(
                WeightedEntry({ newSimpleCap() }, 10),
                WeightedEntry({ newFancyCap() }, 5),
                WeightedEntry({ newAncientCap() }, 2),
                WeightedEntry({ newSimpleHelm() }, 10),
                WeightedEntry({ newFancyHelm() }, 5),
                WeightedEntry({ newAncientHelm() }, 2),
                WeightedEntry({ newSimpleSallet() }, 10),
                WeightedEntry({ newFancySallet() }, 5),
                WeightedEntry({ newAncientSallet() }, 2),

                WeightedEntry({ newSimpleJacket() }, 10),
                WeightedEntry({ newFancyJacket() }, 5),
                WeightedEntry({ newAncientJacket() }, 2),
                WeightedEntry({ newSimpleHauberk() }, 10),
                WeightedEntry({ newFancyHauberk() }, 5),
                WeightedEntry({ newAncientHauberk() }, 2),
                WeightedEntry({ newSimpleCuirass() }, 10),
                WeightedEntry({ newFancyCuirass() }, 5),
                WeightedEntry({ newAncientCuirass() }, 2),

                WeightedEntry({ newSimpleGloves() }, 10),
                WeightedEntry({ newFancyGloves() }, 5),
                WeightedEntry({ newAncientGloves() }, 2),
                WeightedEntry({ newSimpleBracers() }, 10),
                WeightedEntry({ newFancyBracers() }, 5),
                WeightedEntry({ newAncientBracers() }, 2),
                WeightedEntry({ newSimpleGauntlets() }, 10),
                WeightedEntry({ newFancyGauntlets() }, 5),
                WeightedEntry({ newAncientGauntlets() }, 2),

                WeightedEntry({ newSimpleShoes() }, 10),
                WeightedEntry({ newFancyShoes() }, 5),
                WeightedEntry({ newAncientShoes() }, 2),
                WeightedEntry({ newSimpleBoots() }, 10),
                WeightedEntry({ newFancyBoots() }, 5),
                WeightedEntry({ newAncientBoots() }, 2),
                WeightedEntry({ newSimpleGreaves() }, 10),
                WeightedEntry({ newFancyGreaves() }, 5),
                WeightedEntry({ newAncientGreaves() }, 2)
        )

        val sample = weightedCollection.sample()!!

        return sample()
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

    fun newSimpleJacket() = newArmorBuilder(Body, SimpleJacket, GameTile.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, TINY_RESISTANCE)))
            .build()

    fun newFancyJacket() = newArmorBuilder(Body, FancyJacket, GameTile.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newAncientJacket() = newArmorBuilder(Body, AncientJacket, GameTile.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleHauberk() = newArmorBuilder(Body, SimpleHauberk, GameTile.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancyHauberk() = newArmorBuilder(Body, FancyHauberk, GameTile.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientHauberk() = newArmorBuilder(Body, AncientHauberk, GameTile.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, LARGE_RESISTANCE)))
            .build()

    fun newSimpleCuirass() = newArmorBuilder(Body, SimpleCuirass, GameTile.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancyCuirass() = newArmorBuilder(Body, FancyCuirass, GameTile.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientCuirass() = newArmorBuilder(Body, AncientCuirass, GameTile.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, HUGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, LARGE_RESISTANCE)))
            .build()

    // HANDS

    fun newSimpleGloves() = newArmorBuilder(Arms, SimpleGloves, GameTile.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, TINY_RESISTANCE)))
            .build()

    fun newFancyGloves() = newArmorBuilder(Arms, FancyGloves, GameTile.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newAncientGloves() = newArmorBuilder(Arms, AncientGloves, GameTile.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleBracers() = newArmorBuilder(Arms, SimpleBracers, GameTile.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyBracers() = newArmorBuilder(Arms, FancyBracers, GameTile.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientBracers() = newArmorBuilder(Arms, AncientBracers, GameTile.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    fun newSimpleGauntlets() = newArmorBuilder(Arms, SimpleGauntlets, GameTile.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyGauntlets() = newArmorBuilder(Arms, FancyGauntlets, GameTile.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientGauntlets() = newArmorBuilder(Arms, AncientGauntlets, GameTile.RARE_HEAVY_ARMOR)
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
     * HELPERS
     */

    private fun newArmorBuilder(equipType: EquippableType, type: EntityType, tile: CharacterTile) = AnyEntityBuilder.newBuilder(type)
            .withAttributes(
                    EntityTile(tile),
                    EntityPosition(),
                    EquippableDetails(equipType))
            .withFacets(Droppable, Takeable, Equippable)
}