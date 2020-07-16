package behavior

import attribute.flag.Obstacle
import attribute.flag.Opaque
import attribute.flag.Opened
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import world.GameContext

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