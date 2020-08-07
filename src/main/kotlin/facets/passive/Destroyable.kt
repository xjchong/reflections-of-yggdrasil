package facets.passive

import attributes.Inventory
import attributes.LootTable
import commands.Destroy
import commands.Drop
import entity.executeBlockingCommand
import entity.getAttribute
import entity.position
import events.Notice
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object Destroyable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Destroy::class) { (context, entity, cause) ->
            context.world.observeSceneBy(entity, "The $entity is destroyed by $cause.", Notice)

            entity.getAttribute(Inventory::class)?.let { inventory ->
                inventory.contents.forEach { item ->
                    item.executeBlockingCommand(Drop(context, item, entity, entity.position))
                }
            }

            entity.getAttribute(LootTable::class)?.let { lootTable ->
                lootTable.table.sample()?.invoke()?.forEach { loot ->
                    loot.executeBlockingCommand(Drop(context, loot, entity, entity.position))
                }
            }

            context.world.removeEntity(entity)

            Consumed
        }
    }
}