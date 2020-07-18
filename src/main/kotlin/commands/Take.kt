package commands

import entity.InventoryOwner
import entity.InventoryOwnerType
import entity.Item
import entity.ItemType
import game.GameContext

data class Take(
        override val context: GameContext,
        override val source: InventoryOwner,
        override val target: Item) : EntityAction<InventoryOwnerType, ItemType>