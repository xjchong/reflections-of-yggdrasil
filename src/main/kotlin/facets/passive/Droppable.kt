package facets.passive

import attributes.Inventory
import commands.Drop
import entity.getAttribute
import extensions.neighbors
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Droppable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Drop::class) { (context, droppable, owner, idealPosition) ->
            owner.getAttribute(Inventory::class)?.let { inventory -> inventory.remove(droppable) }

            with (context.world) {
                var actualDropPosition = idealPosition

                // Attempt to drop the entity somewhere unoccupied. Otherwise, just stack it at the original position.
                fetchBlockAt(idealPosition).ifPresent { block ->
                    if (!block.isUnoccupied) {
                        for (neighborPos in idealPosition.neighbors()) {
                            val neighborBlock = fetchBlockAt(neighborPos).optional

                            if (neighborBlock != null && neighborBlock.isUnoccupied) {
                                actualDropPosition = neighborPos
                                break
                            }
                        }
                    }
                }


                addEntity(droppable, actualDropPosition)
                observeSceneBy(owner, "The $owner drops the $droppable.")
            }

            Consumed
        }
    }
}
