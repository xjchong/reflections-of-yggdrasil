package facets.passive

import attributes.ConsumableDetails
import attributes.EntityTime
import commands.ApplyStatus
import commands.Consume
import entity.getAttribute
import entity.spendTime
import extensions.responseWhenIs
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Consumable : BaseFacet<GameContext>(ConsumableDetails::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Consume::class) { (context, consumable, consumer) ->
            val details = consumable.getAttribute(ConsumableDetails::class)

            consumer.spendTime(EntityTime.CONSUME)
            context.world.observeSceneBy(consumer, "The $consumer consumes the $consumable.")

            details?.effects?.forEach { effect ->
                consumer.executeCommand(ApplyStatus(context, consumable, consumer, effect))
            }

            Consumed
        }
    }
}