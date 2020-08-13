package commands

import entity.GameEntity
import game.GameContext

data class Dig(
    override val context: GameContext,
    val diggable: GameEntity,
    val digger: GameEntity
) : GameCommand(diggable)