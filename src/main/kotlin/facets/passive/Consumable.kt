package facets.passive

import attributes.ConsumableDetails
import attributes.EnergyLevel
import commands.Consume
import entity.getAttribute
import events.Error
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

            val energyLevel = consumer.getAttribute(EnergyLevel::class) ?: run {
                world.observeSceneBy(consumer, "The $consumer doesn't seem interested in the $consumable...", Error)
                return@responseWhenCommandIs Consumed
            }

            val energyValue = consumable.getAttribute(ConsumableDetails::class) ?: run {
                world.observeSceneBy(consumer, "The $consumable doesn't seem to do much for the $consumer...", Error)
                return@responseWhenCommandIs Consumed
            }

            energyLevel.currentEnergy += energyValue.nutrition
            world.observeSceneBy(consumer, "The $consumer consumes the $consumable.")

            Consumed
        }
    }
}