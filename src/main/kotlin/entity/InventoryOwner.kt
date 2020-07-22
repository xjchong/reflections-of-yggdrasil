package entity

import attributes.Inventory
import org.hexworks.amethyst.api.entity.EntityType


interface InventoryOwnerType : EntityType

typealias InventoryOwner = GameEntity<InventoryOwnerType>

val InventoryOwner.inventory: Inventory
    get() = findAttribute(Inventory::class).get()

fun InventoryOwner.addToInventory(content: AnyEntity) = inventory.add(content)

fun InventoryOwner.removeFromInventory(content: AnyEntity) = inventory.remove(content)

