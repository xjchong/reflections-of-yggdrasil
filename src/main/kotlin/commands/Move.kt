package commands

import entity.AnyGameEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D


data class Move(
        override val context: GameContext,
        override val source: AnyGameEntity,
        val position: Position3D
) : GameCommand<EntityType>