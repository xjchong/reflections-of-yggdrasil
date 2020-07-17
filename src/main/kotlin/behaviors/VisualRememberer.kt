package behaviors

import attributes.VisualMemory
import extensions.getAttribute
import extensions.position
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import game.GameContext

object VisualRememberer : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val world = context.world
        val position = entity.position
        val visualMemory = entity.getAttribute(VisualMemory::class) ?: return false

        world.findVisiblePositionsFor(entity).forEach { visiblePos ->
            visualMemory.memory[visiblePos] = world.getEntitySnapshotAt(visiblePos)
        }

        return true
    }
}