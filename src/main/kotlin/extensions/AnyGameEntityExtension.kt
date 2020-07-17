package extensions

import attributes.EntityActions
import attributes.EntityPosition
import attributes.EntitySnapshot
import attributes.EntityTile
import attributes.flag.Obstacle
import attributes.flag.Opaque
import entity.Player
import kotlinx.coroutines.runBlocking
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.zircon.api.data.Tile
import game.GameContext
import kotlin.reflect.KClass


var AnyGameEntity.position
    get() = findAttribute(EntityPosition::class).get().position
    set(value) {
        findAttribute(EntityPosition::class).map {
            it.position = value
        }
    }

val AnyGameEntity.tile: Tile
    get() = this.findAttribute(EntityTile::class).get().tile

val AnyGameEntity.isObstacle: Boolean
    get() = findAttribute(Obstacle::class).isPresent

val AnyGameEntity.isPlayer: Boolean
    get() = this.type == Player

val AnyGameEntity.isOpaque: Boolean
    get() = this.findAttribute(Opaque::class).isPresent

val AnyGameEntity.snapshot: EntitySnapshot
    get() = EntitySnapshot(type, getAttribute(EntityTile::class)?.tile)

fun <T : Attribute> AnyGameEntity.getAttribute(klass: KClass<T>): T? = findAttribute(klass).optional

fun AnyGameEntity.tryActionsOn(context: GameContext, target: AnyGameEntity): Response {
    var result: Response = Pass

    findAttribute(EntityActions::class).map {
        val actions = it.createActionsFor(context, this, target)

        for (action in actions) {
            runBlocking {
                if (target.executeCommand(action) is Consumed) {
                    result = Consumed
                }
            }

            if (result == Consumed) break
        }
    }

    return result
}

