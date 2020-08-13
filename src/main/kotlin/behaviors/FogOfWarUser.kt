package behaviors

import entity.GameEntity
import game.GameContext

object FogOfWarUser : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: GameEntity, context: GameContext): Boolean {
        context.world.updateFowAt(entity)
        return true
    }
}