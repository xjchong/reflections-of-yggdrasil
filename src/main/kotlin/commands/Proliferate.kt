package commands

import entity.GameEntity
import game.GameContext


data class Proliferate(
    override val context: GameContext,
    val proliferatable: GameEntity
) : GameCommand(proliferatable)