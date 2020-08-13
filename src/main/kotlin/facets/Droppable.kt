package facets

import attributes.EntityTime
import attributes.Inventory
import commands.Drop
import entity.getAttribute
import entity.spendTime
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
        return command.responseWhenCommandIs(Drop::class) { (context, droppable, dropper, idealPosition) ->
            dropper.getAttribute(Inventory::class)?.remove(droppable)

            with (context.world) {
                var actualDropPosition = idealPosition

                // Attempt to drop the entity somewhere unoccupied. Otherwise, just stack it at the original position.
                fetchBlockAt(idealPosition).ifPresent { block ->
                    if (block.entities.any { it != dropper }) {
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
                dropper.spendTime(EntityTime.DROP)
                observeSceneBy(dropper, "The $dropper drops the $droppable.")
            }

            Consumed
        }
    }
}
