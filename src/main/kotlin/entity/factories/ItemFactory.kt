package entity.factories

import attributes.*
import builders.newGameEntityOfType
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
     * UTILITY
     */

    fun newRandomWeapon(): AnyEntity {
        return when (Random.nextInt(3)) {
            0 -> newDagger()
            1 -> newStaff()
            else -> newSword()
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
}