package extensions

import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.entity.EntityType
import kotlin.reflect.KClass


suspend fun <T: Command<out EntityType, GameContext>> Command<out EntityType, GameContext>.responseWhenIs(
        klass: KClass<T>, fn: suspend (T) -> Response): Response {
    return if (klass.isInstance(this)) {
        fn(this as T)
    } else {
        Pass
    }
}
