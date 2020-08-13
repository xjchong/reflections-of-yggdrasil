package commands

import entity.AnyEntity
import game.GameContext

data class Open(
        override val context: GameContext,
        override val source: AnyEntity,
        val target: AnyEntity
) : PlannableCommand(executor = target)