package commands

import entity.AnyGameEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Wield(
        override val context: GameContext,
        override val source: AnyGameEntity, // Equipment.
        override val target: AnyGameEntity // Equipment user.
) : EntityAction<EntityType, EntityType>