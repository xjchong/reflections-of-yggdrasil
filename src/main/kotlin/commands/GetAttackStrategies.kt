package commands

import attributes.AttackStrategies
import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType


data class GetAttackStrategies(
        override val context: GameContext,
        override val source: AnyEntity
) : GameCommand<EntityType>

data class GetAttackStrategiesResponse(
        override val context: GameContext,
        override val source: AnyEntity,
        val strategies: AttackStrategies
) : GameCommand<EntityType>
