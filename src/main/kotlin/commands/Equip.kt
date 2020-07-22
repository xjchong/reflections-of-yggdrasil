package commands

import entity.Equipment
import entity.EquipmentType
import entity.EquipmentUser
import entity.EquipmentUserType
import game.GameContext

data class Equip(
        override val context: GameContext,
        override val source: EquipmentUser,
        override val target: Equipment
) : EntityAction<EquipmentUserType, EquipmentType>