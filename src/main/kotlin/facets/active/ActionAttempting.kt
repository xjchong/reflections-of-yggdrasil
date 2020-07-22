package facets.active

import attributes.EntityActions
import commands.AttemptAnyAction
import entity.AnyEntity
import entity.executeBlockingCommand
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object ActionAttempting : BaseFacet<GameContext>(EntityActions::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(AttemptAnyAction::class) { (context, source, position) ->
            val targets = context.world.fetchEntitiesAt(position)

            for (target in targets.reversed()) {
                if (source.tryActionsOn(context, target) == Consumed) {
                    return@responseWhenCommandIs Consumed
                }
            }

            Pass
        }
    }

    private fun AnyEntity.tryActionsOn(context: GameContext, target: AnyEntity): Response {
        var result: Response = org.hexworks.amethyst.api.Pass

        findAttribute(attributes.EntityActions::class).map {
            val actions = it.createActionsFor(context, this, target)

            for (action in actions) {
                if (target.executeBlockingCommand(action) is Consumed) {
                    result = org.hexworks.amethyst.api.Consumed
                    break
                }
            }
        }

        return result
    }
}