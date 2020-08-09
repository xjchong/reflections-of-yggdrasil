package entity

import attributes.*
import attributes.flag.Obstacle
import block.GameBlock
import extensions.optional
import facets.passive.Movable
import game.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.modifier.Modifier
import kotlin.reflect.KClass


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
    get() = tile.character.toString()

// This can be updated to support other types of 'vision'.
val AnyEntity.sensedPositions: Set<Position3D>
    get() {
        val senses = getAttribute(Senses::class) ?: return setOf()

        return senses.sensedPositions
    }

// This can be updated depending on the entity, and what it is trying to pass.
fun AnyEntity.canPass(block: GameBlock): Boolean {
    for (entity in block.entities) {
        if (entity.findFacet(Movable::class).isPresent
                && entity.getAttribute(EntityPosition::class)?.lastPosition != entity.position) continue
        if (entity.findAttribute(Obstacle::class).isPresent) return false
    }

    return true
}

fun AnyEntity.isAlliedWith(entity: AnyEntity): Boolean {
    if (this.id == entity.id) return true
    if (entity.getAttribute(CombatStats::class) == null) return true

    val factionDetails = getAttribute(FactionDetails::class) ?: return false
    val otherFaction = entity.getAttribute(FactionDetails::class)?.ownFaction

    return factionDetails.alliedFactions.contains(otherFaction)
}

fun AnyEntity.isEnemiesWith(entity: AnyEntity): Boolean {
    if (this.id == entity.id) return false
    if (entity.getAttribute(CombatStats::class) == null) return false

    val factionDetails = getAttribute(FactionDetails::class) ?: return true
    val otherFaction = entity.getAttribute(FactionDetails::class)?.ownFaction

    return !factionDetails.alliedFactions.contains(otherFaction)
            && !factionDetails.neutralFactions.contains(otherFaction)
}

fun AnyEntity.addTileModifiers(vararg modifiers: Modifier) {
    getAttribute(EntityTile::class)?.run {
        tile = tile.withAddedModifiers(*modifiers)
    }
}

fun AnyEntity.removeTileModifiers(vararg modifiers: Modifier) {
    getAttribute(EntityTile::class)?.run {
        tile = tile.withRemovedModifiers(*modifiers)
    }
}

fun <T : Attribute> AnyEntity.getAttribute(klass: KClass<T>): T? = findAttribute(klass).optional

inline fun <reified T : Attribute> AnyEntity.hasAttribute(): Boolean {
    return findAttribute(T::class).isPresent
}

inline fun <reified T : BaseFacet<GameContext>> AnyEntity.hasFacet(): Boolean {
    return findFacet(T::class).isPresent
}

inline fun <reified T : BaseFacet<GameContext>> AnyEntity.whenFacetIs(fn: (AnyEntity) -> Unit) {
    if (findFacet(T::class).isPresent) fn(this)
}

inline fun <reified T : BaseBehavior<GameContext>> AnyEntity.hasBehavior(): Boolean {
    return findBehavior(T::class).isPresent
}
