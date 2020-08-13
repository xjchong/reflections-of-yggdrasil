package commands

import entity.GameEntity
import game.GameContext

data class Destroy(
        override val context: GameContext,
        val destroyable: GameEntity,
        val cause: String = "natural causes."
) : GameCommand(destroyable)
