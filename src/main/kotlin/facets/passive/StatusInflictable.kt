package facets.passive

import attributes.CombatStats
import attributes.StatusDetails
import commands.Guard
import entity.getAttribute
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object StatusInflictable : BaseFacet<GameContext>(StatusDetails::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        var response: Response = Pass

        command.whenCommandIs(Guard::class) { (context, entity) ->
            entity.findAttribute(StatusDetails::class).ifPresent { statusDetails ->
                statusDetails.guard = 1
                entity.getAttribute(CombatStats::class)?.dockStamina(5)
                context.world.observeSceneBy(entity, "The $entity guards against attacks!")
                response = Consumed
            }

            true
        }

        return response
    }
}