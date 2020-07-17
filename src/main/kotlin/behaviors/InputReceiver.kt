package behaviors

import command.Move
import extensions.position
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import game.GameContext

object InputReceiver : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context
        val startPos = player.position

        if (uiEvent is KeyboardEvent) {
            val endPos = when (uiEvent.code) {
                KeyCode.RIGHT -> startPos.withRelativeX(1)
                KeyCode.DOWN -> startPos.withRelativeY(1)
                KeyCode.LEFT -> startPos.withRelativeX(-1)
                KeyCode.UP -> startPos.withRelativeY(-1)
                else -> startPos
            }

            player.executeCommand(Move(context, player, endPos))
        }

        return true
    }
}