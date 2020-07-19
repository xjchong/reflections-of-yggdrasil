package entity

import attributes.Equipment
import org.hexworks.amethyst.api.entity.EntityType


interface EquipmentWearerType: EntityType
typealias EquipmentWearer = GameEntity<EquipmentWearerType>

val EquipmentWearer.equipment: Equipment
        get() = findAttribute(Equipment::class).get()