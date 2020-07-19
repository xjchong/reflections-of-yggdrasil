package commands

import entity.CombatItem
import entity.CombatItemType
import entity.EquipmentWearer
import entity.EquipmentWearerType
import game.GameContext

data class Equip(
        override val context: GameContext,
        override val source: EquipmentWearer,
        override val target: CombatItem
) : EntityAction<EquipmentWearerType, CombatItemType>