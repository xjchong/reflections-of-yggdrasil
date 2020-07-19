package entity

import attributes.NutritionalValue


interface FoodType : ItemType

val GameEntity<FoodType>.energy: Int
    get() = findAttribute(NutritionalValue::class).get().energy