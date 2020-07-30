package commands

import attributes.AttackStrategies
import entity.AnyEntity
import game.GameContext
import models.AttackStrategy
import org.hexworks.amethyst.api.entity.EntityType


data class PickAttackStrategy(
        override val context: GameContext,
        override val source: AnyEntity,
        val target: AnyEntity,
        val strategies: AttackStrategies
) : GameCommand<EntityType>

data class PickAttackStrategyResponse(
        override val context: GameContext,
        override val source: AnyEntity,
        val strategy: AttackStrategy?
) : GameCommand<EntityType>