package behaviors

import attributes.flag.Obstacle
import attributes.flag.Opaque
import attributes.flag.Opened
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import game.GameContext

object Barrier : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
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