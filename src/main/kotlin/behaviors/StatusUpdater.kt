package behaviors

import GameColor
import attributes.CombatStats
import attributes.KillTarget
import attributes.StatusDetails
import attributes.Vigilance
import entity.AnyEntity
import entity.getAttribute
import entity.position
import events.Flavor
import game.GameContext
import models.Poison

object StatusUpdater : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        with (entity) {
            updateGuard()
            updatePoison(context)
            updateStamina()
        }

        return true
    }

    private fun AnyEntity.updateGuard() {
        val details = getAttribute(StatusDetails::class) ?: return

        details.guard = (details.guard - 1).coerceAtLeast(0)
    }

    private fun AnyEntity.updatePoison(context: GameContext) {
        val details = getAttribute(StatusDetails::class) ?: return
        if (details.poison <= 0) return

        details.poison = (details.poison - 1).coerceAtLeast(0)

        getAttribute(CombatStats::class)?.let {
            val damage = Poison.damage

            context.world.observeSceneBy(this, "The $this takes $damage poison damage...", Flavor)
            context.world.flash(position, GameColor.POISON_FLASH)
            it.dockHealth(damage)
        }
    }

    private fun AnyEntity.updateStamina() {
        // Entities with a target they are actively trying to kill don't regen stamina.
        if (getAttribute(KillTarget::class)?.target != null) return

        // Entities that are not fully relaxed don't regen stamina.
        val alertLevel = getAttribute(Vigilance::class)?.alertLevel
        if (alertLevel != null && alertLevel > 0) return

        getAttribute(CombatStats::class)?.gainStamina(20)
    }
}