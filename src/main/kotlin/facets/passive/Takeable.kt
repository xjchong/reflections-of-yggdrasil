package facets.passive

import attributes.CoinPouch
import attributes.CoinValue
import attributes.Inventory
import commands.Take
import entity.getAttribute
import events.Critical
import events.Error
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
            val coinPouch = taker.getAttribute(CoinPouch::class)
            val coinValue = takeable.getAttribute(CoinValue::class)

            if (coinValue != null && coinPouch?.addValue(coinValue.value) == true) {
                world.removeEntity(takeable)
                world.observeSceneBy(taker, "The $taker pockets the $takeable worth ${coinValue.value} en.")
            } else if (inventory == null || inventory.isFull) {
                world.observeSceneBy(taker, "The $taker has no room for the $takeable.", Critical)
            } else if (inventory.add(takeable)) {
                world.removeEntity(takeable)
                world.observeSceneBy(taker, "The $taker picks up the $takeable.")
            } else {
                world.observeSceneBy(taker, "The $taker's inventory can't hold the $takeable for some reason.", Error)
            }

            Consumed
        }
    }
}