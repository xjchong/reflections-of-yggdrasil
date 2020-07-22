package commands

import entity.AnyGameEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Take(
        override val context: GameContext,
        override val source: AnyGameEntity, // Entity being taken.
        override val target: AnyGameEntity
) : EntityAction<EntityType, EntityType>