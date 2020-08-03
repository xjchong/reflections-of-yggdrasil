package behaviors

import attributes.flag.BlocksSmell
import attributes.flag.Obstacle
import attributes.flag.Opaque
import attributes.flag.Opened
import entity.AnyEntity
import game.GameContext

object Barrier : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        with(entity.asMutableEntity()) {
            findAttribute(Opened::class).ifPresent {
                removeAttribute(Obstacle)
                findAttribute(Opaque::class).ifPresent {
                    removeAttribute(Opaque)
                }
                findAttribute(BlocksSmell::class).ifPresent {
                    removeAttribute(BlocksSmell)
                }
            }
        }

        return true
    }
}