package commands

import entity.GameEntity
import game.GameContext


data class Close(
        override val context: GameContext,
        val closeable: GameEntity,
        val closer: GameEntity
) : GameCommand(closeable)