package entity

import attributes.NutritionalValue


interface FoodType : ItemType

typealias Food = GameEntity<FoodType>

val GameEntity<FoodType>.energy: Int
    get() = findAttribute(NutritionalValue::class).get().energy