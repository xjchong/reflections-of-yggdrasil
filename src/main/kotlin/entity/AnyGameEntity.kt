package entity

import attributes.*
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
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.CharacterTile
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

val AnyGameEntity.tile: CharacterTile
    get() = this.findAttribute(EntityTile::class).get().tile

val AnyGameEntity.symbol: String
    get() = this.findAttribute(EntityTile::class).get().tile.character.toString()

val AnyGameEntity.isObstacle: Boolean
    get() = findAttribute(Obstacle::class).isPresent

val AnyGameEntity.isTakeable: Boolean
    get() = findFacet(Takeable::class).isPresent

val AnyGameEntity.isPlayer: Boolean
    get() = this.type == Player

val AnyGameEntity.isOpaque: Boolean
    get() = this.findAttribute(Opaque::class).isPresent

val AnyGameEntity.attackRating: Int
    get() {
        val combatantRating = getAttribute(CombatStats::class)?.attackRating ?: 0
        val equipmentRating = getAttribute(Equipments::class)?.attackRating ?: 0
        val itemRating = getAttribute(CombatStats::class)?.attackRating ?: 0
        return combatantRating + equipmentRating + itemRating
    }

val AnyGameEntity.defenseRating: Int
    get() {
        val combatantRating = getAttribute(CombatStats::class)?.defenseRating ?: 0
        val equipmentRating = getAttribute(Equipments::class)?.defenseRating ?: 0
        val itemRating = getAttribute(CombatStats::class)?.defenseRating ?: 0
        return combatantRating + equipmentRating + itemRating
    }

fun <T : Attribute> AnyGameEntity.getAttribute(klass: KClass<T>): T? = findAttribute(klass).optional

fun AnyGameEntity.syntaxFor(owner: AdaptableSyntax, subKey: String? = null): String {
    val syntax = getAttribute(EntitySyntax::class)?.getFor(owner, subKey)
    return syntax ?: owner.defaultSyntax(subKey)
}

fun AnyGameEntity.executeBlockingCommand(command: Command<out EntityType, GameContext>): Response {
    return runBlocking { executeCommand(command) }
}

inline fun <reified T : EntityType> Iterable<AnyGameEntity>.filterType() : List<Entity<T, GameContext>> {
    return filter { T::class.isSuperclassOf(it.type::class) }.toList() as List<Entity<T, GameContext>>
}

inline fun <reified T : EntityType> AnyGameEntity.whenTypeIs(fn: GameEntity<T>.() -> Unit) {
    if (T::class.isSuperclassOf(this.type::class)) {
        (this as GameEntity<T>).run(fn)
    }
}

inline fun <reified T : EntityType> AnyGameEntity.isType(): Boolean {
    return T::class.isSuperclassOf(this.type::class)
}
