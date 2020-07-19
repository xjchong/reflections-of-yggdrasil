package facets.passive

import attributes.EntitySyntax
import commands.Destroy
import commands.Drop
import entity.*
import events.logGameEvent
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
        return command.responseWhenCommandIs(Destroy::class) { (context, entity, cause) ->
            logGameEvent("The $entity is ${entity.syntaxFor(Destructible)} by $cause.")

            entity.whenTypeIs<InventoryOwnerType> {
                inventory.items.forEach { item ->
                    item.executeBlockingCommand(Drop(context, this, item, position))
                }
            }

            context.world.removeEntity(entity)

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