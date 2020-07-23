package facets.passive

import attributes.EntitySyntax
import attributes.Inventory
import commands.Destroy
import commands.Drop
import entity.executeBlockingCommand
import entity.getAttribute
import entity.position
import entity.syntaxFor
import events.Notice
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
            context.world.observeSceneBy(entity, "The $entity is ${entity.syntaxFor(Destructible)} by $cause.", Notice)

            entity.getAttribute(Inventory::class)?.let { inventory ->
                inventory.contents.forEach { item ->
                    item.executeBlockingCommand(Drop(context, item, entity, entity.position))
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