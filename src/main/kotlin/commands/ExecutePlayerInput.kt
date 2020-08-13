package commands

import entity.GameEntity
import events.GameInputEvent
import game.GameContext


data class ExecutePlayerInput(
        override val context: GameContext,
        val playerControllable: GameEntity,
        val inputEvent: GameInputEvent
) : GameCommand(playerControllable)