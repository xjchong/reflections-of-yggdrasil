package behaviors

import attributes.flag.Obstacle
import attributes.flag.Opaque
import attributes.flag.Opened
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType

object Barrier : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.isBackground) return true

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