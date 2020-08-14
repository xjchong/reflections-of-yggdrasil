package facets

import attributes.EntityTime
import attributes.facet.ProliferatableDetails
import commands.Proliferate
import entity.getAttribute
import entity.position
import entity.spendTime
import extensions.responseWhenIs
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Size3D

object Proliferatable : BaseFacet<GameContext>(ProliferatableDetails::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Proliferate::class) { (context, proliferatable) ->
            val details = proliferatable.getAttribute(ProliferatableDetails::class) ?: return@responseWhenIs Pass
            val world = context.world

            if (Math.random() < details.factor) {
                world.findEmptyLocationWithin(
                    offset = proliferatable.position.withRelativeX(-1).withRelativeY(-1),
                    size = Size3D.create(3, 3, 0)
                ).ifPresent { emptyPosition ->
                    world.addEntity(details.proliferate(details), emptyPosition)
                    details.factor *= details.decayRate
                }
            }

            proliferatable.spendTime(EntityTime.PROLIFERATE)
            Consumed
        }
    }
}