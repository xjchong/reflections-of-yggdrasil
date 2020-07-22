package commands

import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D

data class Drop(
        override val context: GameContext,
        override val source: AnyEntity,
        override val target: AnyEntity,
        val position: Position3D
) : EntityAction<EntityType, EntityType>