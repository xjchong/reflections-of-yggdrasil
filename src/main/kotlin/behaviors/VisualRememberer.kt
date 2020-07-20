package behaviors

import attributes.VisualMemory
import entity.getAttribute
import entity.position
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType

object VisualRememberer : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.inBackground) return true

        val world = context.world
        val position = entity.position
        val visualMemory = entity.getAttribute(VisualMemory::class) ?: return false

        world.findVisiblePositionsFor(entity).forEach { visiblePos ->
            world.fetchBlockAt(visiblePos).ifPresent { block ->
                visualMemory.remember(visiblePos, world.turn, block.entities.toList())
            }
        }

        return true
    }
}