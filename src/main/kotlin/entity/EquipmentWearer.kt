package entity

import attributes.Equipments
import org.hexworks.amethyst.api.entity.EntityType


interface EquipmentUserType: EntityType
typealias EquipmentUser = GameEntity<EquipmentUserType>

val EquipmentUser.equipments: Equipments
        get() = findAttribute(Equipments::class).get()