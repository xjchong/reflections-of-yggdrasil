package entity

import attributes.ItemCombatStats


interface CombatItemType : ItemType
typealias CombatItem = GameEntity<CombatItemType>

val CombatItem.attackRating: Int
    get() = findAttribute(ItemCombatStats::class).get().attackRating

val CombatItem.defenseRating: Int
    get() = findAttribute(ItemCombatStats::class).get().defenseRating

val CombatItem.combatType: String
    get() = findAttribute(ItemCombatStats::class).get().combatType


interface ArmorType : CombatItemType
typealias Armor = GameEntity<ArmorType>

interface WeaponType : CombatItemType
typealias Weapon = GameEntity<WeaponType>


