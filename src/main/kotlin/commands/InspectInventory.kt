package commands

import entity.GameEntity
import game.GameContext

data class InspectInventory(
        override val context: GameContext,
        val inventoryOwner: GameEntity
) : GameCommand(inventoryOwner)