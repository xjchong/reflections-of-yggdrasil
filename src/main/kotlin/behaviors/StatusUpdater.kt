package behaviors

import GameColor
import attributes.CombatStats
import attributes.StatusDetails
import entity.AnyEntity
import entity.getAttribute
import entity.position
import events.Flavor
import game.GameContext
import models.Poison

object StatusUpdater : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        entity.findAttribute(StatusDetails::class).ifPresent { details ->
            details.guard = (details.guard - 1).coerceAtLeast(0)
            updatePoison(context, entity, details)
        }

        return true
    }

    private fun updatePoison(context: GameContext, entity: AnyEntity, details: StatusDetails) {
        if (details.poison <= 0) return

        details.poison = (details.poison - 1).coerceAtLeast(0)

        entity.getAttribute(CombatStats::class)?.run {
            val damage = Poison.damage

            context.world.observeSceneBy(entity, "The $entity takes $damage poison damage...", Flavor)
            context.world.flash(entity.position, GameColor.POISON_FLASH)
            dockHealth(damage)
        }
    }
}