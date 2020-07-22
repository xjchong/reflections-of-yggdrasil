package facets.passive

import attributes.Inventory
import commands.Take
import entity.getAttribute
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object Takeable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Take::class) { (context, takeable, taker) ->
            val world = context.world
            val inventory = taker.getAttribute(Inventory::class)

            if (inventory == null || inventory.isFull) {
                world.observeSceneBy(taker, "The $taker has no room for the $takeable.")
            } else if (inventory.add(takeable)) {
                world.removeEntity(takeable)
                world.observeSceneBy(taker, "The $taker picks up the $takeable.")
            } else {
                world.observeSceneBy(taker, "The $taker's inventory can't hold the $takeable for some reason.")
            }

            Consumed
        }
    }
}