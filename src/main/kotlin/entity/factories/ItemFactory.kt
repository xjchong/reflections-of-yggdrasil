package entity.factories

import attributes.CoinValue
import attributes.ConsumableDetails
import attributes.EntityPosition
import attributes.EntityTile
import builders.newGameEntityOfType
import constants.GameTile
import entity.AnyEntity
import entity.BatMeat
import entity.SmallCoins
import facets.Consumable
import facets.Droppable
import facets.Takeable
import models.Heal
import models.Poison
import models.StatusEffect
import kotlin.random.Random


object ItemFactory {

    fun newRandomTreasure(): AnyEntity {
        return newSmallCoins()
    }

    /**
     * TREASURE
     */

    fun newSmallCoins() = newGameEntityOfType(SmallCoins) {
        attributes(
                CoinValue(Random.nextInt(5, 20)),
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
        facets(
            Consumable,
            Droppable,
            Takeable
        )
    }
}