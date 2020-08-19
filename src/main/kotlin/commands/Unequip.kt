package commands

import entity.GameEntity
import game.GameContext


data class Unequip(
    override val context: GameContext,
    val equippable: GameEntity,
    val equipper: GameEntity
) : GameCommand(equippable)
