package behaviors

import attributes.FocusTarget
import entity.AnyEntity
import entity.getAttribute
import entity.position
import entity.sensedPositions
import extensions.optional
import game.GameContext

object FocusTargetUser : ForegroundBehavior(FocusTarget::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        if (context.inBackground) return true

        val focusTarget = entity.getAttribute(FocusTarget::class) ?: return true
        val targetPosition = focusTarget.target.optional?.position ?: return true

        if (!entity.sensedPositions.contains(targetPosition)) {
            focusTarget.clearTarget()
        }

        return true
    }
}