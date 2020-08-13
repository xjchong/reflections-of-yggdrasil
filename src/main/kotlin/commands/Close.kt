package commands

import entity.AnyEntity
import game.GameContext


data class Close(
        override val context: GameContext,
        override val source: AnyEntity,
        val target: AnyEntity
) : PlannableCommand(executor = target)