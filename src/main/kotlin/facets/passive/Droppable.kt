package facets.passive

import attributes.Inventory
import commands.Drop
import entity.getAttribute
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Droppable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Drop::class) { (context, droppable, owner, position) ->
            owner.getAttribute(Inventory::class)?.let { inventory -> inventory.remove(droppable) }

            with (context.world) {
                addEntity(droppable, position)
                observeSceneBy(owner, "The $owner drops the $droppable.")
            }

            Consumed
        }
    }
}
