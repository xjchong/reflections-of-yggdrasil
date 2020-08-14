package behaviors

import GameColor
import attributes.facet.AttackableDetails
import attributes.KillTarget
import attributes.facet.StatusApplicableDetails
import attributes.Vigilance
import entity.GameEntity
import entity.getAttribute
import entity.position
import events.Flavor
import game.GameContext
import models.Poison

object StatusUpdater : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: GameEntity, context: GameContext): Boolean {
        with (entity) {
            updateGuard()
            updatePoison(context)
            updateStamina()
        }

        return true
    }

    private fun GameEntity.updateGuard() {
        val details = getAttribute(StatusApplicableDetails::class) ?: return

        details.guard = (details.guard - 1).coerceAtLeast(0)
    }

    private fun GameEntity.updatePoison(context: GameContext) {
        val details = getAttribute(StatusApplicableDetails::class) ?: return
        if (details.poison <= 0) return

        details.poison = (details.poison - 1).coerceAtLeast(0)

        getAttribute(AttackableDetails::class)?.let {
            val damage = Poison.damage

            context.world.observeSceneBy(this, "The $this takes $damage poison damage...", Flavor)
            context.world.flash(position, GameColor.POISON_FLASH)
            it.dockHealth(damage)
        }
    }

    private fun GameEntity.updateStamina() {
        val combatStats = getAttribute(AttackableDetails::class) ?: return

        val regenAmount = when {
            getAttribute(KillTarget::class)?.target != null -> 2 // Entities with an active target.
            getAttribute(Vigilance::class)?.alertLevel ?: 0 > 0 -> 2 // Entities still considered in combat.
            else -> 20
        }

        combatStats.gainStamina(regenAmount)
    }
}