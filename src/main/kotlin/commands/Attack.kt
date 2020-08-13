package commands

import entity.GameEntity
import game.GameContext
import models.AttackStrategy


data class Attack(
        override val context: GameContext,
        val attackable: GameEntity,
        val attacker: GameEntity,
        val strategy: AttackStrategy
) : GameCommand(attackable)