package commands

import entity.GameEntity
import game.GameContext
import org.hexworks.zircon.api.data.Position3D

data class Throw(
    override val context: GameContext,
    val throwable: GameEntity,
    val thrower: GameEntity,
    val path: List<Position3D>
) : GameCommand(throwable)