package commands

import entity.AnyEntity
import events.GameInputEvent
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType


data class ExecutePlayerInput(
        override val context: GameContext,
        override val source: AnyEntity,
        val inputEvent: GameInputEvent
) : GameCommand<EntityType>