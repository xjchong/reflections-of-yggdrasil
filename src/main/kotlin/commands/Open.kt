package commands

import entity.GameEntity
import game.GameContext

data class Open(
        override val context: GameContext,
        val openable: GameEntity,
        val opener: GameEntity
) : GameCommand(openable)