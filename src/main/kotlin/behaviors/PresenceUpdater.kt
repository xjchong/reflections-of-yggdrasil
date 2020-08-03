package behaviors

import entity.AnyEntity
import game.GameContext


object PresenceUpdater : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        return context.world.updatePresence(entity)
    }
}