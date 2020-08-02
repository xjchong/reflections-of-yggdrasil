package behaviors

import attributes.CombatStats
import attributes.KillTarget
import attributes.Vigilance
import entity.AnyEntity
import entity.getAttribute
import game.GameContext


object StaminaRegenerator : ForegroundBehavior(CombatStats::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        if (context.inBackground) return true

        // Entities with a target they are actively trying to kill don't regen stamina.
        if (entity.getAttribute(KillTarget::class)?.target != null) return true

        // Entities that are not fully relaxed don't regen stamina.
        val alertLevel = entity.getAttribute(Vigilance::class)?.alertLevel
        if (alertLevel != null && alertLevel > 0) return true

        entity.getAttribute(CombatStats::class)?.gainStamina(5)
        return true
    }
}