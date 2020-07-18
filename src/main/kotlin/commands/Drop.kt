package commands

import entity.InventoryOwner
import entity.InventoryOwnerType
import entity.Item
import entity.ItemType
import game.GameContext
import org.hexworks.zircon.api.data.Position3D

data class Drop(
        override val context: GameContext,
        override val source: InventoryOwner,
        override val target: Item,
        val position: Position3D
) : EntityAction<InventoryOwnerType, ItemType>