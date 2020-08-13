package commands

import entity.GameEntity
import game.GameContext


data class Guard(
        override val context: GameContext,
        val guarder: GameEntity
) : GameCommand(guarder)
