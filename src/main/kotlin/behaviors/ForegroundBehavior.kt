package behaviors

import entity.GameEntity
import game.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import kotlin.reflect.KClass

/**
 * Only updates the [GameEntity] when the [GameContext.event] type is in the foreground.
 */
abstract class ForegroundBehavior(
        vararg mandatoryAttribute: KClass<out Attribute>) : BaseBehavior<GameContext>(*mandatoryAttribute) {

    final override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        return if (context.inBackground) true
        else foregroundUpdate(entity, context)
    }

    abstract suspend fun foregroundUpdate(entity: GameEntity, context: GameContext): Boolean
}