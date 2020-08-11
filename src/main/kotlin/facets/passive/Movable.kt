package facets.passive

import attributes.EntityTime
import commands.Move
import entity.hasFacet
import entity.position
import entity.spendTime
import entity.tile
import events.Critical
import extensions.optional
import facets.active.InputReceiving
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Movable : BaseFacet<GameContext>() {
    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Move::class) { (context, entity, position) ->
            var result: Response = Pass
            val world = context.world
            val currentBlock = world.fetchBlockAt(entity.position).optional ?: return@responseWhenCommandIs Pass

            world.fetchBlockAt(position).ifPresent { block ->
                val oldPosition = entity.position
                if (block.transfer(entity, currentBlock)) {
                    entity.spendTime(EntityTime.MOVE)
                    if (!entity.hasFacet<InputReceiving>()) {
                        world.motionBlur(oldPosition, entity.tile.foregroundColor)
                    }

                    result = Consumed
                } else if (entity.findFacet(InputReceiving::class).isPresent) {
                    world.observeSceneBy(entity, "The $entity can't move there...", Critical)
                }
            }

            result
        }
    }
}