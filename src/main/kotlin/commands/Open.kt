package commands

import attributes.EntityTime
import entity.AnyEntity
import game.GameContext

data class Open(
        override val context: GameContext,
        override val source: AnyEntity,
        val target: AnyEntity
) : PlannableCommand(executor = target, timeCost = EntityTime.OPEN)