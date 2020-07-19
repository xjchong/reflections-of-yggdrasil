package commands

import entity.AnyGameEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Destroy(
        override val context: GameContext,
        override val source: AnyGameEntity,
        val cause: String = "natural causes."
) : GameCommand<EntityType>
