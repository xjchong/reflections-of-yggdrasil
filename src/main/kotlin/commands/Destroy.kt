package commands

import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Destroy(
        override val context: GameContext,
        override val source: AnyEntity,
        val cause: String = "natural causes."
) : GameCommand<EntityType>
