package behaviors

import attributes.Vision
import entity.AnyEntity
import entity.getAttribute
import entity.position
import game.GameContext

object VisionUser : ForegroundBehavior(Vision::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val vision = entity.getAttribute(Vision::class) ?: return true

        vision.visiblePositions.clear()
        vision.visiblePositions.addAll(context.world.findVisiblePositionsFor(entity.position, vision.radius))

        return true
    }
}