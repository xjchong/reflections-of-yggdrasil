package facet

import command.Destroy
import event.logGameEvent
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import world.GameContext

object Destructible : BaseFacet<GameContext>() {
    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Destroy::class) { (context, attacker, target, cause) ->
            context.world.removeEntity(target)
            logGameEvent("$target is destroyed by $cause")

            Consumed
        }
    }
}