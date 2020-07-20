package entity

import attributes.EntityTile
import attributes.ItemIcon
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.GraphicalTile


interface ItemType: EntityType

typealias Item = GameEntity<ItemType>

val Item.tile: CharacterTile
    get() = findAttribute(EntityTile::class).get().tile

val Item.iconTile: GraphicalTile
    get() = findAttribute(ItemIcon::class).get().iconTile
