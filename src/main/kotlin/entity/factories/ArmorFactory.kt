package entity.factories

import attributes.*
import attributes.facet.*
import block.GameTile
import builders.AnyEntityBuilder
import constants.GameTileRepo
import entity.*
import facets.Droppable
import facets.Equippable
import facets.Takeable
import models.*
import org.hexworks.amethyst.api.entity.EntityType
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

    fun newRandomArmor(): GameEntity {
        val weightedCollection: WeightedCollection<() -> GameEntity> = WeightedCollection(
                WeightedEntry(10) { newSimpleCap() },
                WeightedEntry(5) { newFancyCap() },
                WeightedEntry(2) { newAncientCap() },
                WeightedEntry(10) { newSimpleHelm() },
                WeightedEntry(5) { newFancyHelm() },
                WeightedEntry(2) { newAncientHelm() },
                WeightedEntry(10) { newSimpleSallet() },
                WeightedEntry(5) { newFancySallet() },
                WeightedEntry(2) { newAncientSallet() },

                WeightedEntry(10) { newSimpleJacket() },
                WeightedEntry(5) { newFancyJacket() },
                WeightedEntry(2) { newAncientJacket() },
                WeightedEntry(10) { newSimpleHauberk() },
                WeightedEntry(5) { newFancyHauberk() },
                WeightedEntry(2) { newAncientHauberk() },
                WeightedEntry(10) { newSimpleCuirass() },
                WeightedEntry(5) { newFancyCuirass() },
                WeightedEntry(2) { newAncientCuirass() },

                WeightedEntry(10) { newSimpleGloves() },
                WeightedEntry(5) { newFancyGloves() },
                WeightedEntry(2) { newAncientGloves() },
                WeightedEntry(10) { newSimpleBracers() },
                WeightedEntry(5) { newFancyBracers() },
                WeightedEntry(2) { newAncientBracers() },
                WeightedEntry(10) { newSimpleGauntlets() },
                WeightedEntry(5) { newFancyGauntlets() },
                WeightedEntry(2) { newAncientGauntlets() },

                WeightedEntry(10) { newSimpleShoes() },
                WeightedEntry(5) { newFancyShoes() },
                WeightedEntry(2) { newAncientShoes() },
                WeightedEntry(10) { newSimpleBoots() },
                WeightedEntry(5) { newFancyBoots() },
                WeightedEntry(2) { newAncientBoots() },
                WeightedEntry(10) { newSimpleGreaves() },
                WeightedEntry(5) { newFancyGreaves() },
                WeightedEntry(2) { newAncientGreaves() }
        )

        val sample = weightedCollection.sample()!!

        return sample()
    }

    fun newRandomBody(): GameEntity {
        val weightedCollection: WeightedCollection<() -> GameEntity> = WeightedCollection(
                WeightedEntry(10) { newSimpleJacket() },
                WeightedEntry(5) { newFancyJacket() },
                WeightedEntry(2) { newAncientJacket() },
                WeightedEntry(10) { newSimpleHauberk() },
                WeightedEntry(5) { newFancyHauberk() },
                WeightedEntry(2) { newAncientHauberk() },
                WeightedEntry(10) { newSimpleCuirass() },
                WeightedEntry(5) { newFancyCuirass() },
                WeightedEntry(2) { newAncientCuirass() }
        )

        val sample = weightedCollection.sample()!!

        return sample()
    }

    // HEAD

    fun newSimpleCap() = newArmorBuilder(Head, SimpleCap, GameTileRepo.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, RELIABLE, TINY_RESISTANCE)))
            .build()

    fun newFancyCap() = newArmorBuilder(Head, FancyCap, GameTileRepo.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newAncientCap() = newArmorBuilder(Head, AncientCap, GameTileRepo.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleHelm() = newArmorBuilder(Head, SimpleHelm, GameTileRepo.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancyHelm() = newArmorBuilder(Head, FancyHelm, GameTileRepo.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientHelm() = newArmorBuilder(Head, AncientHelm, GameTileRepo.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, RELIABLE, LARGE_RESISTANCE),
                            Resistance(Bash, RELIABLE, LARGE_RESISTANCE)))
            .build()

    fun newSimpleSallet() = newArmorBuilder(Head, SimpleSallet, GameTileRepo.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancySallet() = newArmorBuilder(Head, FancySallet, GameTileRepo.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientSallet() = newArmorBuilder(Head, AncientSallet, GameTileRepo.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, RELIABLE, HUGE_RESISTANCE),
                            Resistance(Stab, RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, RELIABLE, LARGE_RESISTANCE)))
            .build()

    // BODY

    fun newSimpleJacket() = newArmorBuilder(Body, SimpleJacket, GameTileRepo.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, TINY_RESISTANCE)))
            .build()

    fun newFancyJacket() = newArmorBuilder(Body, FancyJacket, GameTileRepo.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newAncientJacket() = newArmorBuilder(Body, AncientJacket, GameTileRepo.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleHauberk() = newArmorBuilder(Body, SimpleHauberk, GameTileRepo.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancyHauberk() = newArmorBuilder(Body, FancyHauberk, GameTileRepo.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientHauberk() = newArmorBuilder(Body, AncientHauberk, GameTileRepo.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, LARGE_RESISTANCE)))
            .build()

    fun newSimpleCuirass() = newArmorBuilder(Body, SimpleCuirass, GameTileRepo.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, TINY_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newFancyCuirass() = newArmorBuilder(Body, FancyCuirass, GameTileRepo.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newAncientCuirass() = newArmorBuilder(Body, AncientCuirass, GameTileRepo.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, HUGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, MODERATE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, LARGE_RESISTANCE)))
            .build()

    // HANDS

    fun newSimpleGloves() = newArmorBuilder(Arms, SimpleGloves, GameTileRepo.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, TINY_RESISTANCE)))
            .build()

    fun newFancyGloves() = newArmorBuilder(Arms, FancyGloves, GameTileRepo.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newAncientGloves() = newArmorBuilder(Arms, AncientGloves, GameTileRepo.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleBracers() = newArmorBuilder(Arms, SimpleBracers, GameTileRepo.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyBracers() = newArmorBuilder(Arms, FancyBracers, GameTileRepo.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientBracers() = newArmorBuilder(Arms, AncientBracers, GameTileRepo.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    fun newSimpleGauntlets() = newArmorBuilder(Arms, SimpleGauntlets, GameTileRepo.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyGauntlets() = newArmorBuilder(Arms, FancyGauntlets, GameTileRepo.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientGauntlets() = newArmorBuilder(Arms, AncientGauntlets, GameTileRepo.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, HUGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    // LEGS

    fun newSimpleShoes() = newArmorBuilder(Legs, SimpleShoes, GameTileRepo.COMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, TINY_RESISTANCE)))
            .build()

    fun newFancyShoes() = newArmorBuilder(Legs, FancyShoes, GameTileRepo.UNCOMMON_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newAncientShoes() = newArmorBuilder(Legs, AncientShoes, GameTileRepo.RARE_LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newSimpleBoots() = newArmorBuilder(Legs, SimpleBoots, GameTileRepo.COMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyBoots() = newArmorBuilder(Legs, FancyBoots, GameTileRepo.UNCOMMON_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientBoots() = newArmorBuilder(Legs, AncientBoots, GameTileRepo.RARE_MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    fun newSimpleGreaves() = newArmorBuilder(Legs, SimpleGreaves, GameTileRepo.COMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, TINY_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, SMALL_RESISTANCE)))
            .build()

    fun newFancyGreaves() = newArmorBuilder(Legs, FancyGreaves, GameTileRepo.UNCOMMON_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newAncientGreaves() = newArmorBuilder(Legs, AncientGreaves, GameTileRepo.RARE_HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, HUGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, LARGE_RESISTANCE)))
            .build()

    /**
     * HELPERS
     */

    private fun newArmorBuilder(equipType: EquippableType, type: EntityType, tile: GameTile) = AnyEntityBuilder.newBuilder(type)
            .withAttributes(
                    EntityTile(tile),
                    EntityPosition(),
                EquippableDetails(equipType)
            )
            .withFacets(
                Droppable,
                Takeable,
                Equippable
            )
}