package facets.passive

import facets.active.InputReceiving
import commands.Move
import entity.position
import events.Critical
import extensions.optional
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
            val currentBlock = world.fetchBlockAt(source.position).optional ?: return@responseWhenCommandIs Pass

            world.fetchBlockAt(position).ifPresent { block ->
                if (block.transfer(source, currentBlock)) {
                    result = Consumed
                } else if (source.findFacet(InputReceiving::class).isPresent) {
                    world.observeSceneBy(source, "The $source can't move there...", Critical)
                }
            }

            result
        }
    }
}