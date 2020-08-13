package behaviors

import attributes.AutoTakeDetails
import commands.Take
import entity.GameEntity
import entity.getAttribute
import entity.position
import game.GameContext

object AutoTaker : ForegroundBehavior(AutoTakeDetails::class) {

    override suspend fun foregroundUpdate(entity: GameEntity, context: GameContext): Boolean {
        val details = entity.getAttribute(AutoTakeDetails::class) ?: return false

        context.world.fetchEntitiesAt(entity.position).minus(entity).filter {
            it.attributes.any { attribute -> details.desiredAttributes.contains(attribute::class) }
        }.forEach {
            it.executeCommand(Take(context, it, entity))
        }

        return true
    }
}