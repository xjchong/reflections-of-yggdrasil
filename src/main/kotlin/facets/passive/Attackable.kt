package facets.passive

import commands.Attack
import commands.Destroy
import entity.combatStats
import entity.executeBlockingCommand
import entity.isPlayer
import entity.whenDead
import events.logGameEvent
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

            val damage = (attacker.combatStats.attackRating - target.combatStats.defenseRating)
                    .coerceAtLeast(0)

            with (target) {
                combatStats.health -= damage

                logGameEvent("The $attacker hits the $this for $damage!")

                whenDead {
                    executeBlockingCommand(Destroy(context, this, cause = "the $attacker"))
                }
            }

            Consumed
        }
    }
}