package facets.active

import commands.Take
import entity.executeBlockingCommand
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object ItemTaking : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Take::class) { (context, inventoryOwner, item) ->
            item.executeBlockingCommand(Take(context, inventoryOwner, item))

            Consumed
        }
    }
}
