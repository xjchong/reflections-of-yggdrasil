package command

import entity.AnyGameEntity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D
import game.GameContext


data class Move(
        override val context: GameContext,
        override val source: AnyGameEntity,
        val position: Position3D) : GameCommand<EntityType>