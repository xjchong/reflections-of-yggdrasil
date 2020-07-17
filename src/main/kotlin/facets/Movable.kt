package facets

import command.Move
import extensions.tryActionsOn
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import game.GameContext

object Movable : BaseFacet<GameContext>() {
    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Move::class) { (context, source, position) ->
            var result: Response = Pass
            val world = context.world

            world.fetchBlockAt(position).map { block ->
                if (block.isObstructed) {
                    result = source.tryActionsOn(context, block.obstacle.get())
                } else if (world.moveEntity(source, position)) {
                    result = Consumed
                }
            }

            result
        }
    }
}