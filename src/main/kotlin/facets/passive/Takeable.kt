package facets.passive

import commands.Take
import entity.addToInventory
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object Takeable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Take::class) { (context, inventoryOwner, item) ->
            with (context.world) {
                if (inventoryOwner.addToInventory(item)) {
                    removeEntity(item)
                    observeSceneBy(inventoryOwner, "The $inventoryOwner picks up the $item")
                }
            }

            Consumed
        }
    }
}