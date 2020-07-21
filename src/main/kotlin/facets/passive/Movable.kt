package facets.passive

import commands.Move
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Movable : BaseFacet<GameContext>() {
    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Move::class) { (context, source, position) ->
            var result: Response = Pass
            val world = context.world

            world.fetchBlockAt(position).ifPresent { block ->
                if (block.isObstructed.not() && world.moveEntity(source, position)) {
                    result = Consumed
                }
            }

            result
        }
    }
}