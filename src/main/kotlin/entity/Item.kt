package entity

import attributes.EntityTile
import attributes.ItemIcon
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.GraphicalTile
import org.hexworks.zircon.api.data.Tile


interface ItemType: EntityType

typealias Item = GameEntity<ItemType>

val Item.tile: Tile
    get() = findAttribute(EntityTile::class).get().tile

val Item.iconTile: GraphicalTile
    get() = findAttribute(ItemIcon::class).get().iconTile
