package entity.factories

import attributes.*
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

    fun newLeatherTunic() = newChestArmorBuilder(LeatherTunic, GameTile.LIGHT_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                            Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)))
            .build()

    fun newChainmail() = newChestArmorBuilder(Chainmail, GameTile.MEDIUM_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                            Resistance(Bash, RELIABLE, SMALL_RESISTANCE)))
            .build()

    fun newPlatemail() = newChestArmorBuilder(Platemail, GameTile.HEAVY_ARMOR)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Stab, VERY_RELIABLE, LARGE_RESISTANCE),
                            Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)))
            .build()

    fun newJacket() = newChestArmorBuilder(Jacket, GameTile.JACKET)
            .withAddedAttributes(
                    Resistances(
                            Resistance(Cut, VERY_UNRELIABLE, SMALL_RESISTANCE),
                            Resistance(Stab, VERY_UNRELIABLE, SMALL_RESISTANCE),
                            Resistance(Bash, VERY_UNRELIABLE, TINY_RESISTANCE)))
            .build()

    private fun newChestArmorBuilder(type: EntityType, tile: CharacterTile) = AnyEntityBuilder.newBuilder(type)
            .withAttributes(
                    EntityTile(tile),
                    EntityPosition(),
                    EquippableDetails(Chest))
            .withFacets(Droppable, Takeable, Equippable)
}