package facets

import attributes.EntityTime
import commands.Dig
import entity.spendTime
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Diggable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Dig::class) { (context, diggable, digger) ->
            context.world.removeEntity(diggable)
            digger.spendTime(EntityTime.DIG)
            Consumed
        }
    }
}