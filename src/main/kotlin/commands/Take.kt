package commands

import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Take(
        override val context: GameContext,
        override val source: AnyEntity, // Entity being taken.
        override val target: AnyEntity
) : EntityAction<EntityType, EntityType>