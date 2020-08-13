package commands

import entity.AnyEntity
import game.GameContext
import models.AttackStrategy


data class Attack(
        override val context: GameContext,
        override val source: AnyEntity,
        val target: AnyEntity,
        val strategy: AttackStrategy
) : PlannableCommand(executor = target)