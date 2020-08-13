package commands

import entity.GameEntity
import game.GameContext
import org.hexworks.zircon.api.data.Position3D


data class Move(
        override val context: GameContext,
        val movable: GameEntity,
        val nextPosition: suspend () -> Position3D?
) : GameCommand(movable)