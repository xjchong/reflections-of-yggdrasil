package commands

import entity.GameEntity
import game.GameContext

data class Consume(
    override val context: GameContext,
    val consumable: GameEntity,
    val consumer: GameEntity
) : GameCommand(consumable)