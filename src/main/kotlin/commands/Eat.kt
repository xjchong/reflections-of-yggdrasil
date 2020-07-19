package commands

import entity.EnergyUserType
import entity.Food
import entity.FoodType
import entity.GameEntity
import game.GameContext

data class Eat(
        override val context: GameContext,
        override val source: GameEntity<EnergyUserType>,
        override val target: Food
) : EntityAction<EnergyUserType, FoodType>