package facets.passive

import attributes.EntitySyntax
import commands.Destroy
import entity.syntaxFor
import event.logGameEvent
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

interface AdaptableSyntax {

    fun defaultSyntax(subKey: String? = null): String
}

object Destructible : BaseFacet<GameContext>(), AdaptableSyntax {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Destroy::class) { (context, attacker, target, cause) ->
            context.world.removeEntity(target)
            logGameEvent("The $target is ${target.syntaxFor(Destructible)} by $cause.")

            Consumed
        }
    }

    override fun defaultSyntax(subKey: String?): String {
        return "killed"
    }

    fun withDestroyedVerb(verb: String, syntax: EntitySyntax): Destructible {
        syntax.putFor(this, verb)
        return this
    }
}