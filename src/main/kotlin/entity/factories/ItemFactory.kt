package entity.factories

import attributes.*
import constants.GameTile
import entity.*
import facets.passive.Consumable
import facets.passive.Droppable
import facets.passive.Equippable
import facets.passive.Takeable
import models.Heal
import models.Poison
import models.StatusEffect
import kotlin.random.Random


object ItemFactory {

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
                ConsumableDetails(effects = listOf(
                        StatusEffect(Heal, 20, 0.9),
                        StatusEffect(Poison, 5, 0.1)
                )))
        facets(Consumable, Droppable, Takeable)
    }

    /**
     * EQUIPMENT
     */

    fun newDagger() = newGameEntityOfType(Dagger) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.DAGGER),
                EquippableDetails(OneHanded,
                        reliability = 0.6,
                        efficiency = 1.1
                ))

        facets(Droppable, Takeable, Equippable)
    }

    fun newSword() = newGameEntityOfType(Sword) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.SWORD),
                EquippableDetails(OneHanded,
                        reliability = 0.8,
                        efficiency = 1.2
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newStaff() = newGameEntityOfType(Staff) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.STAFF),
                EquippableDetails(TwoHanded,
                        reliability = 0.7,
                        efficiency = 1.1
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newLightArmor() = newGameEntityOfType(LightArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.LIGHT_ARMOR),
                EquippableDetails(Chest,
                        reliability = 0.8,
                        efficiency = 0.2
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newMediumArmor() = newGameEntityOfType(MediumArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.MEDIUM_ARMOR),
                EquippableDetails(Chest,
                        reliability = 0.8,
                        efficiency = 0.3
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newHeavyArmor() = newGameEntityOfType(HeavyArmor) {
        attributes(
                EntityPosition(),
                EntityTile(GameTile.HEAVY_ARMOR),
                EquippableDetails(Chest,
                        reliability = 0.8,
                        efficiency = 0.35
                ))
        facets(Droppable, Takeable, Equippable)
    }

    fun newJacket() = newGameEntityOfType(Jacket) {
        attributes(
                EntityTile(GameTile.JACKET),
                EntityPosition(),
                EquippableDetails(Chest,
                        reliability = 0.6,
                        efficiency = 0.1
                ))
        facets(Droppable, Takeable, Equippable)
    }
}