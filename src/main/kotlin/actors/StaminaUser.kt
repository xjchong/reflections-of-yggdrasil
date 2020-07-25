package actors

import attributes.CombatStats
import entity.getAttribute
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseActor
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType


object StaminaUser : BaseActor<GameContext>(CombatStats::class) {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        // TODO: If there are no enemies in sight, then regen stamina fully/very quickly.

        // If there are still enemies, then regen slowly. TODO: Add varying amounts of stamina regen.
        entity.getAttribute(CombatStats::class)?.regenStamina(5)
        return true
    }

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return Pass
    }
}