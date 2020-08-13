package commands

import entity.GameEntity
import game.GameContext


data class ExecuteAiPlans(
    override val context: GameContext,
    val aiControllable: GameEntity
) : GameCommand(aiControllable)
