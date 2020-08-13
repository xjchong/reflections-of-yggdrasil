package commands

import entity.GameEntity
import game.GameContext
import models.StatusEffect


data class ApplyStatus(
        override val context: GameContext,
        val applicable: GameEntity,
        val applier: GameEntity,
        val effect: StatusEffect
) : GameCommand(applier)