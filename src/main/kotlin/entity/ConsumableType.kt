package entity

import attributes.NutritionalValue
import org.hexworks.amethyst.api.entity.EntityType


interface ConsumableType : EntityType

val GameEntity<ConsumableType>.energy: Int
    get() = findAttribute(NutritionalValue::class).get().energy
