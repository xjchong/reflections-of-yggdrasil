package commands

import entity.AnyEntity
import game.GameContext
import org.hexworks.zircon.api.data.Position3D


data class Move(
        override val context: GameContext,
        override val source: AnyEntity,
        val position: suspend () -> Position3D?
) : PlannableCommand(executor = source)