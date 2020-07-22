package commands

import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D


data class Move(
        override val context: GameContext,
        override val source: AnyEntity,
        val position: Position3D
) : GameCommand<EntityType>