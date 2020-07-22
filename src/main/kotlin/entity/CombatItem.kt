package entity

import org.hexworks.amethyst.api.entity.EntityType


interface EquipmentType : EntityType
interface WeaponType : EquipmentType
interface ArmorType : EquipmentType

typealias Equipment = GameEntity<EquipmentType>
typealias Weapon = GameEntity<WeaponType>
typealias Armor = GameEntity<ArmorType>
