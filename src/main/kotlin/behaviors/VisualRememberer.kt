package behaviors

import attributes.VisualMemory
import entity.AnyEntity
import entity.getAttribute
import entity.position
import game.GameContext

object VisualRememberer : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
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