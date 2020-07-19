package facets.active

import attributes.EnergyLevel
import commands.Eat
import entity.energy
import entity.energyLevel
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object FoodEating : BaseFacet<GameContext>(EnergyLevel::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Eat::class) { (context, entity, food) ->
            entity.energyLevel.currentEnergy += food.energy

            context.world.observeSceneBy(entity, "The $entity eats the $food")
            Consumed
        }
    }
}