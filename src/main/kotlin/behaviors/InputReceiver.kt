package behaviors

import commands.Move
import entity.AnyGameEntity
import entity.position
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import utilities.DebugConfig

object InputReceiver : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context
        val playerPos = player.position

        if (uiEvent is KeyboardEvent) {
            when (uiEvent.code) {
                KeyCode.RIGHT -> player.executeMove(context, playerPos.withRelativeX(1))
                KeyCode.DOWN -> player.executeMove(context, playerPos.withRelativeY(1))
                KeyCode.LEFT -> player.executeMove(context, playerPos.withRelativeX(-1))
                KeyCode.UP -> player.executeMove(context, playerPos.withRelativeY(-1))

                KeyCode.BACKSLASH -> DebugConfig.apply { shouldRevealWorld = !shouldRevealWorld }
            }
        }

        return true
    }

    private suspend fun AnyGameEntity.executeMove(context: GameContext, nextPosition: Position3D) {
        executeCommand(Move(context, this, nextPosition))
    }
}