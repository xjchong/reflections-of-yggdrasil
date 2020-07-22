package commands

import entity.AnyEntity
import org.hexworks.amethyst.api.entity.EntityType
import game.GameContext

data class Open(
        override val context: GameContext,
        override val source: AnyEntity,
        override val target: AnyEntity
) : EntityAction<EntityType, EntityType>