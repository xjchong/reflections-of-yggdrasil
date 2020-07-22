package commands

import entity.AnyGameEntity
import entity.InventoryOwner
import entity.InventoryOwnerType
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D

data class Drop(
        override val context: GameContext,
        override val source: InventoryOwner,
        override val target: AnyGameEntity,
        val position: Position3D
) : EntityAction<InventoryOwnerType, EntityType>