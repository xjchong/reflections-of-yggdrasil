package commands

import entity.GameEntity
import game.GameContext

data class Take(
        override val context: GameContext,
        val takeable: GameEntity,
        val taker: GameEntity
) : GameCommand(takeable)