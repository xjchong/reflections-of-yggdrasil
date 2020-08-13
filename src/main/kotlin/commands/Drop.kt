package commands

import entity.GameEntity
import game.GameContext
import org.hexworks.zircon.api.data.Position3D

data class Drop(
    override val context: GameContext,
    val droppable: GameEntity,
    val dropper: GameEntity,
    val position: Position3D
) : GameCommand(droppable)