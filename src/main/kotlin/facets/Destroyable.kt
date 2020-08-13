package facets

import attributes.Inventory
import attributes.LootTable
import commands.Destroy
import commands.Drop
import entity.getAttribute
import entity.position
import events.Notice
import extensions.responseWhenIs
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object Destroyable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Destroy::class) { (context, destroyable, cause) ->
            context.world.observeSceneBy(destroyable, "The $destroyable is destroyed by $cause.", Notice)

            val inventory = destroyable.getAttribute(Inventory::class)
            val lootTable = destroyable.getAttribute(LootTable::class)

            inventory?.contents?.forEach { item ->
                item.executeCommand(Drop(context, item, destroyable, destroyable.position))
            }

            lootTable?.table?.sample()?.invoke()?.forEach { loot ->
                loot.executeCommand(Drop(context, loot, destroyable, destroyable.position))
            }

            context.world.removeEntity(destroyable)

            Consumed
        }
    }
}