package behavior

import attribute.flag.Obstacle
import attribute.flag.Opened
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import world.GameContext

object Barrier : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (entity.findAttribute(Opened::class).isPresent) {
            entity.asMutableEntity().removeAttribute(Obstacle)
        }

        return true
    }
}