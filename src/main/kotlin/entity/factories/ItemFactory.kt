package entity.factories

import attributes.ConsumableDetails
import attributes.EntityPosition
import attributes.EntityTile
import builders.newGameEntityOfType
import constants.GameTile
import entity.AnyEntity
import entity.BatMeat
import entity.En
import facets.passive.Consumable
import facets.passive.Droppable
import facets.passive.Takeable
import models.Heal
import models.Poison
import models.StatusEffect


object ItemFactory {

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
}