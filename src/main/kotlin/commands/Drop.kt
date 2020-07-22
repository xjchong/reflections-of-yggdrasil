package commands

import entity.AnyGameEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D

data class Drop(
        override val context: GameContext,
        override val source: AnyGameEntity,
        override val target: AnyGameEntity,
        val position: Position3D
) : EntityAction<EntityType, EntityType>