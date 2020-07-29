package commands

import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType


data class Heal(
        override val context: GameContext,
        override val source: AnyEntity,
        override val target: AnyEntity,
        val amount: Int
) : EntityAction<EntityType, EntityType>