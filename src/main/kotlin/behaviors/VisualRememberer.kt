package behaviors

import attributes.Senses
import attributes.VisualMemory
import entity.AnyEntity
import entity.getAttribute
import entity.position
import game.GameContext

object VisualRememberer : ForegroundBehavior(VisualMemory::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        val position = entity.position
        val visualMemory = entity.getAttribute(VisualMemory::class) ?: return false
        val senses = entity.getAttribute(Senses::class) ?: return true

        senses.visiblePositions.forEach { visiblePos ->
            world.fetchBlockAt(visiblePos).ifPresent { block ->
                visualMemory.remember(visiblePos, world.turn, block.entities.toList())
                block.rememberAs(visualMemory.getMemoryAt(visiblePos))
            }
        }

        return true
    }
}