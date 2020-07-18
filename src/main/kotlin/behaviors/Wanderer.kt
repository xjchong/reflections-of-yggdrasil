package behaviors

import commands.Move
import entity.position
import extensions.neighbors
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType


object Wanderer : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.inBackground) return true

        val position = entity.position

        if (!position.isUnknown) {
            entity.executeCommand(Move(context, entity, position.neighbors().first()))

            return true
        }

        return false
    }
}