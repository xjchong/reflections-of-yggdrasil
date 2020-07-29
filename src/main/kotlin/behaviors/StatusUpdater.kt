package behaviors

import attributes.StatusDetails
import entity.AnyEntity
import game.GameContext

object StatusUpdater : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        entity.findAttribute(StatusDetails::class).ifPresent { it.update() }

        return true
    }
}