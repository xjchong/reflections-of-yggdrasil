package commands

import entity.AnyGameEntity
import entity.InventoryOwner
import entity.InventoryOwnerType
import game.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Take(
        override val context: GameContext,
        override val source: InventoryOwner,
        override val target: AnyGameEntity) : EntityAction<InventoryOwnerType, EntityType>