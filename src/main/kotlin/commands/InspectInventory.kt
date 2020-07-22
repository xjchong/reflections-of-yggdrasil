package commands

import entity.InventoryOwner
import entity.InventoryOwnerType
import game.GameContext

data class InspectInventory(
        override val context: GameContext,
        override val source: InventoryOwner
) : GameCommand<InventoryOwnerType>