package commands

import entity.EnergyUserType
import entity.GameEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType


data class ExpendEnergy(
        override val context: GameContext,
        override val source: GameEntity<EnergyUserType>,
        val energy: Int
) : GameCommand<EntityType>