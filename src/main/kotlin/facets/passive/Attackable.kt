package facets.passive

import attributes.CombatStats
import commands.Attack
import commands.Destroy
import entity.attackRating
import entity.executeBlockingCommand
import entity.getAttribute
import entity.isPlayer
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Attackable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Attack::class) { (context, attacker, target) ->
            if (!attacker.isPlayer && !target.isPlayer) return@responseWhenCommandIs Pass

            target.getAttribute(CombatStats::class)?.let { combatStats ->
                val damage = attacker.attackRating.coerceAtLeast(10)

                combatStats.health -= damage

                context.world.observeSceneBy(attacker, "The $attacker hits the $target for $damage!")

                if (combatStats.health <= 0) {
                    target.executeBlockingCommand(Destroy(context, target, cause = "the $attacker"))
                }
            }

            Consumed
        }
    }
}