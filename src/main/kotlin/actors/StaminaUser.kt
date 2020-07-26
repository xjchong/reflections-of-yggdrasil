package actors

import attributes.CombatStats
import attributes.KillTarget
import attributes.Vigilance
import entity.getAttribute
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseActor
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType


object StaminaUser : BaseActor<GameContext>(CombatStats::class) {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.inBackground) return true

        // Entities with a target they are actively trying to kill don't regen stamina.
        if (entity.getAttribute(KillTarget::class)?.target != null) return true

        // Entities that are not fully relaxed don't regen stamina.
        val alertLevel = entity.getAttribute(Vigilance::class)?.alertLevel
        if (alertLevel != null && alertLevel > 0) return true

        entity.getAttribute(CombatStats::class)?.regenStamina(5)
        return true
    }

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return Pass
    }
}