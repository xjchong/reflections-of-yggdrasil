package facets.passive

import attributes.ConsumableDetails
import commands.Consume
import entity.getAttribute
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Consumable : BaseFacet<GameContext>(ConsumableDetails::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Consume::class) { (context, consumable, consumer) ->
            context.world.observeSceneBy(consumer, "The $consumer consumes the $consumable.")

            consumable.getAttribute(ConsumableDetails::class)?.run {
                execute(context, consumable, consumer)
            }

            Consumed
        }
    }
}