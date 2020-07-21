package behaviors

import attributes.flag.Obstacle
import attributes.flag.Opaque
import attributes.flag.Opened
import entity.AnyGameEntity
import game.GameContext

object Barrier : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyGameEntity, context: GameContext): Boolean {
        with(entity.asMutableEntity()) {
            findAttribute(Opened::class).ifPresent {
                removeAttribute(Obstacle)
                findAttribute(Opaque::class).ifPresent {
                    removeAttribute(Opaque)
                }
            }
        }

        return true
    }
}