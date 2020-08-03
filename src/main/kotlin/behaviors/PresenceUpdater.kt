package behaviors

import attributes.Presence
import entity.AnyEntity
import entity.getAttribute
import entity.position
import game.GameContext


object PresenceUpdater : ForegroundBehavior(Presence::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        if (entity.position == entity.getAttribute(Presence::class)?.lastPosition) return true

        return context.world.updatePresence(entity)
    }
}