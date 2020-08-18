package commands

import entity.GameEntity
import game.GameContext


data class InspectEquipments(
    override val context: GameContext,
    val equipmentsOwner: GameEntity
) : GameCommand(equipmentsOwner)