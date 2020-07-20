package entity

import org.hexworks.amethyst.api.entity.EntityType


interface ItemType: EntityType
typealias Item = GameEntity<ItemType>
