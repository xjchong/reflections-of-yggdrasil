package facets

import attributes.MoveLog
import commands.Move
import entity.getAttribute
import entity.hasFacet
import entity.position
import entity.tile
import events.Critical
import extensions.optional
import extensions.responseWhenIs
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Movable : BaseFacet<GameContext>() {
    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Move::class) { (context, entity, position) ->
            var result: Response = Pass
            val world = context.world
            val nextPosition = position() ?: return@responseWhenIs Pass
            val currentBlock = world.fetchBlockAt(entity.position).optional ?: return@responseWhenIs Pass

            world.fetchBlockAt(nextPosition).ifPresent { block ->
                val oldPosition = entity.position
                if (block.transfer(entity, currentBlock)) {
                    if (!entity.hasFacet<PlayerControllable>()) {
                        world.motionBlur(oldPosition, entity.tile.foregroundColor)
                    }

                    entity.getAttribute(MoveLog::class)?.logVisit(nextPosition)

                    result = Consumed
                } else if (entity.findFacet(PlayerControllable::class).isPresent) {
                    world.observeSceneBy(entity, "The $entity can't move there...", Critical)
                }
            }

            result
        }
    }
}