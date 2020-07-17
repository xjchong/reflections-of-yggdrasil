package attributes

import commands.EntityAction
import entity.AnyGameEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType
import game.GameContext
import kotlin.reflect.KClass

class EntityActions(
    private vararg val actions: KClass<out EntityAction<out EntityType, out EntityType>>
) : Attribute {

    fun createActionsFor(context: GameContext, source: AnyGameEntity, target: AnyGameEntity):
            Iterable<EntityAction<out EntityType, out EntityType>> {
        return actions.map {
            try {
                it.constructors.first().call(context, source, target)
            } catch (e: Exception) {
                throw IllegalArgumentException("Can't create EntityAction. Does it have the proper constructor?")
            }
        }
    }
}