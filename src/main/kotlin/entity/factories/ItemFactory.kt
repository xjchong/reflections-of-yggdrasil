package entity.factories

import attributes.CoinValue
import attributes.EntityPosition
import attributes.EntityTile
import attributes.facet.ConsumableDetails
import builders.newGameEntityOfType
import constants.GameTileRepo
import entity.BatMeat
import entity.GameEntity
import entity.SmallCoins
import facets.Consumable
import facets.Droppable
import facets.Takeable
import models.Heal
import models.Poison
import models.StatusEffect
import kotlin.random.Random


object ItemFactory {

    fun newRandomTreasure(): GameEntity {
        return newSmallCoins()
    }

    /**
     * TREASURE
     */

    fun newSmallCoins() = newGameEntityOfType(SmallCoins) {
        attributes(
                CoinValue(Random.nextInt(5, 20)),
                EntityPosition(),
                EntityTile(GameTileRepo.EN))
        facets(Takeable, Droppable)
    }

    /**
     * CONSUMABLES
     */

    fun newBatMeat() = newGameEntityOfType(BatMeat) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepo.BAT_MEAT),
            ConsumableDetails(
                StatusEffect(Heal, 20, 0.9),
                StatusEffect(Poison, 5, 0.1)
            )
        )
        facets(
            Consumable,
            Droppable,
            Takeable
        )
    }
}