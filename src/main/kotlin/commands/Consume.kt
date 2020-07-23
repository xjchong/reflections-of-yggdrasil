package commands

import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Consume(
        override val context: GameContext,
        override val source: AnyEntity,
        override val target: AnyEntity
) : EntityAction<EntityType, EntityType>