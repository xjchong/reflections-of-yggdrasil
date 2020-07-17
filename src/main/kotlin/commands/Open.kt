package commands

import entity.AnyGameEntity
import org.hexworks.amethyst.api.entity.EntityType
import game.GameContext

data class Open(
        override val context: GameContext,
        override val source: AnyGameEntity,
        override val target: AnyGameEntity
) : EntityAction<EntityType, EntityType>