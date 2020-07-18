package entity

import attributes.EntityActions
import attributes.EntityPosition
import attributes.EntitySnapshot
import attributes.EntityTile
import attributes.flag.Obstacle
import attributes.flag.Opaque
import extensions.optional
import facets.passive.Takeable
import game.GameContext
import kotlinx.coroutines.runBlocking
import org.hexworks.amethyst.api.*
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Tile
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf


typealias AnyGameEntity = Entity<EntityType, GameContext>

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

val AnyGameEntity.isTakeable: Boolean
    get() = findFacet(Takeable::class).isPresent

val AnyGameEntity.isPlayer: Boolean
    get() = this.type == Player

val AnyGameEntity.isOpaque: Boolean
    get() = this.findAttribute(Opaque::class).isPresent

val AnyGameEntity.snapshot: EntitySnapshot
    get() = EntitySnapshot(type, getAttribute(EntityTile::class)?.tile)

fun <T : Attribute> AnyGameEntity.getAttribute(klass: KClass<T>): T? = findAttribute(klass).optional

fun AnyGameEntity.execute(command: Command<out EntityType, GameContext>): Response {
    return runBlocking { executeCommand(command) }
}

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

inline fun <reified T : EntityType> Iterable<AnyGameEntity>.filterType() : List<Entity<T, GameContext>> {
    return filter { T::class.isSuperclassOf(it.type::class) }.toList() as List<Entity<T, GameContext>>
}