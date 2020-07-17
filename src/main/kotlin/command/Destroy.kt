package command

import entity.AnyGameEntity
import org.hexworks.amethyst.api.entity.EntityType
import game.GameContext

data class Destroy(
        override val context: GameContext,
        override val source: AnyGameEntity,
        val target: AnyGameEntity,
        val cause: String = "natural causes."
) : GameCommand<EntityType>
