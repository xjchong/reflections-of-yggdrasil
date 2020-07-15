package facet

import command.Attack
import command.Destroy
import entity.combatStats
import entity.whenDead
import extension.isPlayer
import kotlinx.coroutines.runBlocking
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import world.GameContext

object Attackable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Attack::class) { (context, attacker, target) ->
            if (!attacker.isPlayer && !target.isPlayer) return@responseWhenCommandIs Pass

            val damage = Math.max(0, attacker.combatStats.attackRating - target.combatStats.defenseRating)

            target.combatStats.health -= damage

            target.whenDead {
                runBlocking {
                    target.executeCommand(Destroy(
                        context, attacker, target, cause = "a mortal wound."))
                }
            }

            Consumed
        }
    }
}