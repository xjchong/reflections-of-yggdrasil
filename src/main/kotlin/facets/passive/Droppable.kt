package facets.passive

import commands.Drop
import entity.removeFromInventory
import event.logGameEvent
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Droppable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Drop::class) { (context, inventoryOwner, item, position) ->
            if (inventoryOwner.removeFromInventory(item)) {
                context.world.addEntity(item, position)

                logGameEvent("The $inventoryOwner drops the $item.")
            }

            Consumed
        }
    }
}
