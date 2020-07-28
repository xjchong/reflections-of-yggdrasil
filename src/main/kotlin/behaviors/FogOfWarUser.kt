package behaviors

import entity.AnyEntity
import game.GameContext

object FogOfWarUser : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        context.world.updateFowAt(entity)
        return true
    }
}