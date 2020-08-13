package facets

import attributes.EntityTime
import attributes.MoveLog
import commands.Move
import entity.*
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
        return command.responseWhenIs(Move::class) { (context, movable, nextPosition) ->
            var result: Response = Pass
            val world = context.world
            val nextPosition = nextPosition() ?: return@responseWhenIs Pass
            val currentBlock = world.fetchBlockAt(movable.position).optional ?: return@responseWhenIs Pass

            world.fetchBlockAt(nextPosition).ifPresent { block ->
                val oldPosition = movable.position
                if (block.transfer(movable, currentBlock, world)) {
                    if (!movable.hasFacet<PlayerControllable>()) {
                        world.motionBlur(oldPosition, movable.tile.foregroundColor)
                    }

                    movable.getAttribute(MoveLog::class)?.logVisit(nextPosition)

                    result = Consumed
                } else if (movable.findFacet(PlayerControllable::class).isPresent) {
                    world.observeSceneBy(movable, "The $movable can't move there...", Critical)
                }
            }

            if (result == Consumed) movable.spendTime(EntityTime.MOVE)
            result
        }
    }
}