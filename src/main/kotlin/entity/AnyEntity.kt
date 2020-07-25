package entity

import attributes.EntityPosition
import attributes.EntitySyntax
import attributes.EntityTile
import attributes.Equipments
import attributes.flag.Obstacle
import attributes.flag.Opaque
import extensions.optional
import facets.passive.AdaptableSyntax
import facets.passive.Takeable
import game.GameContext
import kotlinx.coroutines.runBlocking
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.CharacterTile
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf


typealias AnyEntity = Entity<EntityType, GameContext>

var AnyEntity.position
    get() = findAttribute(EntityPosition::class).get().position
    set(value) {
        findAttribute(EntityPosition::class).map {
            it.position = value
        }
    }

val AnyEntity.tile: CharacterTile
    get() = this.findAttribute(EntityTile::class).get().tile

val AnyEntity.symbol: String
    get() = this.findAttribute(EntityTile::class).get().tile.character.toString()

val AnyEntity.isObstacle: Boolean
    get() = findAttribute(Obstacle::class).isPresent

val AnyEntity.isTakeable: Boolean
    get() = findFacet(Takeable::class).isPresent

val AnyEntity.isPlayer: Boolean
    get() = this.type == Player

val AnyEntity.isOpaque: Boolean
    get() = this.findAttribute(Opaque::class).isPresent

val AnyEntity.attackRating: Int
    get() {
        val equipmentRating = getAttribute(Equipments::class)?.attackRating ?: 0
        return equipmentRating
    }

val AnyEntity.defenseRating: Int
    get() {
        val equipmentRating = getAttribute(Equipments::class)?.defenseRating ?: 0
        return equipmentRating
    }

fun <T : Attribute> AnyEntity.getAttribute(klass: KClass<T>): T? = findAttribute(klass).optional

fun AnyEntity.syntaxFor(owner: AdaptableSyntax, subKey: String? = null): String {
    val syntax = getAttribute(EntitySyntax::class)?.getFor(owner, subKey)
    return syntax ?: owner.defaultSyntax(subKey)
}

fun AnyEntity.executeBlockingCommand(command: Command<out EntityType, GameContext>): Response {
    return runBlocking { executeCommand(command) }
}

inline fun <reified T : EntityType> Iterable<AnyEntity>.filterType() : List<Entity<T, GameContext>> {
    return filter { T::class.isSuperclassOf(it.type::class) }.toList() as List<Entity<T, GameContext>>
}

inline fun <reified T : EntityType> AnyEntity.whenTypeIs(fn: GameEntity<T>.() -> Unit) {
    if (T::class.isSuperclassOf(this.type::class)) {
        (this as GameEntity<T>).run(fn)
    }
}

inline fun <reified T : EntityType> AnyEntity.isType(): Boolean {
    return T::class.isSuperclassOf(this.type::class)
}

inline fun <reified T : BaseFacet<GameContext>> AnyEntity.hasFacet(): Boolean {
    return this.findFacet(T::class).isPresent
}

inline fun <reified T : BaseFacet<GameContext>> AnyEntity.whenFacetIs(fn: (AnyEntity) -> Unit) {
    if (this.findFacet(T::class).isPresent) {
        fn(this)
    }
}