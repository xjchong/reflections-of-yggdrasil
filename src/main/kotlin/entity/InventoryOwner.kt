package entity

import attributes.Inventory
import org.hexworks.amethyst.api.entity.EntityType


interface InventoryOwnerType : EntityType

typealias InventoryOwner = GameEntity<InventoryOwnerType>

val InventoryOwner.inventory: Inventory
    get() = findAttribute(Inventory::class).get()

fun InventoryOwner.addToInventory(item: Item) = inventory.add(item)

fun InventoryOwner.removeFromInventory(item: Item) = inventory.remove(item)

