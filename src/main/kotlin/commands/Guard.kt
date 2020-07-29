package commands

import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType


data class Guard(
        override val context: GameContext,
        override val source: AnyEntity
) : GameCommand<EntityType>
