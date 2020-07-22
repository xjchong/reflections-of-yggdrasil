package commands

import entity.ConsumableType
import entity.EnergyUserType
import entity.GameEntity
import game.GameContext

data class Eat(
        override val context: GameContext,
        override val source: GameEntity<EnergyUserType>,
        override val target: GameEntity<ConsumableType>
) : EntityAction<EnergyUserType, ConsumableType>