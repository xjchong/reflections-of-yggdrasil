package extension

import attribute.EntityPosition
import attribute.EntityTile
import attribute.flag.Obstacle
import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Tile
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

fun <T : Attribute> AnyGameEntity.findAttribute(klass: KClass<T>): T =
    findAttribute(klass).orElseThrow {
        NoSuchElementException("Entity '$this' has no property with type '${klass.simpleName}'.")
    }

