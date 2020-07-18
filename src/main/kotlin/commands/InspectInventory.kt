package commands

import entity.InventoryOwner
import entity.InventoryOwnerType
import game.GameContext
import org.hexworks.zircon.api.data.Position3D

data class InspectInventory(
        override val context: GameContext,
        override val source: InventoryOwner,
        val position: Position3D
) : GameCommand<InventoryOwnerType>