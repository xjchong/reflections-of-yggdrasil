package commands

import entity.EnergyUserType
import entity.FoodType
import entity.GameEntity
import game.GameContext

data class Eat(
        override val context: GameContext,
        override val source: GameEntity<EnergyUserType>,
        override val target: GameEntity<FoodType>
) : EntityAction<EnergyUserType, FoodType>