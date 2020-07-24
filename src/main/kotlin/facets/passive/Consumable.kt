package facets.passive

import commands.Consume
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Consumable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Consume::class) { (context, consumable, consumer) ->
            val world = context.world

            world.observeSceneBy(consumer, "The $consumer consumes the $consumable.")

            Consumed
        }
    }
}