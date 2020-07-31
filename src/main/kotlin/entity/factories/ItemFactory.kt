package entity.factories

import attributes.*
import constants.GameTile
import entity.*
import facets.passive.Consumable
import facets.passive.Droppable
import facets.passive.Equippable
import facets.passive.Takeable
import models.*
import kotlin.random.Random


object ItemFactory {

    /**
     * RESISTANCE CONVENIENCE CONSTANTS
     */

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

    /**
     * UTILITY
     */

    fun newRandomEquipment(): AnyEntity {
        return when (Random.nextInt(3)) {
            0 -> newRandomWeapon()
            else -> newRandomArmor()
        }
    }

    fun newRandomWeapon(): AnyEntity {
        return when (Random.nextInt(3)) {
            0 -> newDagger()
            1 -> newStaff()
            else -> newSword()
        }
    }

    fun newRandomArmor(): AnyEntity {
        return when (Random.nextInt(3)) {
            0 -> newLightArmor()
            1 -> newMediumArmor()
            else -> newHeavyArmor()
        }
    }

    fun newRandomTreasure(): AnyEntity {
        return newEn()
    }

    /**
     * TREASURE
     */

    fun newEn() = newGameEntityOfType(En) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.EN))
        facets(Takeable, Droppable)
    }

    /**
     * CONSUMABLES
     */

    fun newBatMeat() = newGameEntityOfType(BatMeat) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.BAT_MEAT),
                ConsumableDetails(
                        StatusEffect(Heal, 20, 0.9),
                        StatusEffect(Poison, 5, 0.1)
                ))
        facets(Consumable, Droppable, Takeable)
    }

    /**
     * EQUIPMENT
     */

    fun newDagger() = newGameEntityOfType(Dagger) {
        attributes(
                AttackStrategies(TechnicalWeaponCut()),
                EntityPosition(),
                EntityTile(GameTile.DAGGER),
                EquippableDetails(OneHanded))

        facets(Droppable, Takeable, Equippable)
    }

    fun newSword() = newGameEntityOfType(Sword) {
        attributes(
                AttackStrategies(BalancedWeaponCut()),
                EntityPosition(),
                EntityTile(GameTile.SWORD),
                EquippableDetails(OneHanded))
        facets(Droppable, Takeable, Equippable)
    }

    fun newStaff() = newGameEntityOfType(Staff) {
        attributes(
                AttackStrategies(TechnicalWeaponBash()),
                EntityPosition(),
                EntityTile(GameTile.STAFF),
                EquippableDetails(TwoHanded))
        facets(Droppable, Takeable, Equippable)
    }

    fun newLightArmor() = newGameEntityOfType(LightArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.LIGHT_ARMOR),
                EquippableDetails(Chest),
                Resistances(
                        Resistance(Cut, OCCASIONAL, MODERATE_RESISTANCE),
                        Resistance(Stab, OCCASIONAL, SMALL_RESISTANCE),
                        Resistance(Bash, OCCASIONAL, MODERATE_RESISTANCE)
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newMediumArmor() = newGameEntityOfType(MediumArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.MEDIUM_ARMOR),
                EquippableDetails(Chest),
                Resistances(
                        Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                        Resistance(Stab, OCCASIONAL, MODERATE_RESISTANCE),
                        Resistance(Bash, RELIABLE, SMALL_RESISTANCE)
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newHeavyArmor() = newGameEntityOfType(HeavyArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.HEAVY_ARMOR),
                EquippableDetails(Chest),
                Resistances(
                        Resistance(Cut, VERY_RELIABLE, LARGE_RESISTANCE),
                        Resistance(Stab, VERY_RELIABLE, LARGE_RESISTANCE),
                        Resistance(Bash, VERY_RELIABLE, MODERATE_RESISTANCE)
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newJacket() = newGameEntityOfType(Jacket) {
        attributes(
                EntityTile(GameTile.JACKET),
                EntityPosition(),
                EquippableDetails(Chest),
                Resistances(
                        Resistance(Cut, VERY_UNRELIABLE, SMALL_RESISTANCE),
                        Resistance(Stab, VERY_UNRELIABLE, SMALL_RESISTANCE),
                        Resistance(Bash, VERY_UNRELIABLE, TINY_RESISTANCE)
                ))
        facets(Droppable, Takeable, Equippable)
    }
}