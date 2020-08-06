package behaviors

import attributes.OpenableDetails
import attributes.flag.BlocksSmell
import attributes.flag.Obstacle
import attributes.flag.Opaque
import entity.AnyEntity
import game.GameContext

object Barrier : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        with (entity.asMutableEntity()) {
            findAttribute(OpenableDetails::class).ifPresent { details ->
                if (details.isOpen)  {
                    removeAttribute(Obstacle)
                    removeAttribute(BlocksSmell)
                    removeAttribute(Opaque)
                } else {
                    addAttribute(Obstacle)
                    addAttribute(BlocksSmell)
                    addAttribute(Opaque)
                }
            }
        }

        return true
    }
}