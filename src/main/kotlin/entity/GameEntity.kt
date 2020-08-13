package entity

import attributes.*
import attributes.flag.IsObstacle
import extensions.optional
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


typealias GameEntity = Entity<EntityType, GameContext>

val GameEntity.position
    get() = getAttribute(EntityPosition::class)?.position ?: Position3D.unknown()

var GameEntity.tile: CharacterTile
    get() = this.findAttribute(EntityTile::class).get().tile
    set(value) {
        getAttribute(EntityTile::class)?.tile = value
    }

val GameEntity.symbol: String
    get() = tile.character.toString()

val GameEntity.time: Long
    get() = getAttribute(EntityTime::class)?.nextUpdateTime?.get() ?: Long.MAX_VALUE

// This can be updated to support other types of 'vision'.
val GameEntity.sensedPositions: Set<Position3D>
    get() {
        val senses = getAttribute(Senses::class) ?: return setOf()

        return senses.sensedPositions
    }

fun GameEntity.spendTime(amount: Long) {
    val speed = getAttribute(CombatStats::class)?.speed ?: 1.0
    getAttribute(EntityTime::class)?.spendTime((amount / speed).toLong())
}

// This can be updated depending on the entity, and what it is trying to pass.
fun GameEntity.canPass(context: GameContext, position: Position3D): Boolean {
    val block = context.world.fetchBlockAt(position).optional ?: return false

    for (entity in block.entities) {
        if (entity.getAttribute(EntityPosition::class)?.staleness(context) ?: 0 <= 1) continue
        if (entity.findAttribute(IsObstacle::class).isPresent) return false
    }

    return true
}

fun GameEntity.isAlliedWith(entity: GameEntity): Boolean {
    if (this.id == entity.id) return true
    if (entity.getAttribute(CombatStats::class) == null) return true

    val factionDetails = getAttribute(FactionDetails::class) ?: return false
    val otherFaction = entity.getAttribute(FactionDetails::class)?.ownFaction

    return factionDetails.alliedFactions.contains(otherFaction)
}

fun GameEntity.isEnemiesWith(entity: GameEntity): Boolean {
    if (this.id == entity.id) return false
    if (entity.getAttribute(CombatStats::class) == null) return false

    val factionDetails = getAttribute(FactionDetails::class) ?: return true
    val otherFaction = entity.getAttribute(FactionDetails::class)?.ownFaction

    return !factionDetails.alliedFactions.contains(otherFaction)
            && !factionDetails.neutralFactions.contains(otherFaction)
}

fun GameEntity.addTileModifiers(vararg modifiers: Modifier) {
    getAttribute(EntityTile::class)?.run {
        tile = tile.withAddedModifiers(*modifiers)
    }
}

fun GameEntity.removeTileModifiers(vararg modifiers: Modifier) {
    getAttribute(EntityTile::class)?.run {
        tile = tile.withRemovedModifiers(*modifiers)
    }
}

fun <T : Attribute> GameEntity.getAttribute(klass: KClass<T>): T? = findAttribute(klass).optional

inline fun <reified T : Attribute> GameEntity.hasAttribute(): Boolean {
    return findAttribute(T::class).isPresent
}

inline fun <reified T : BaseFacet<GameContext>> GameEntity.hasFacet(): Boolean {
    return findFacet(T::class).isPresent
}

inline fun <reified T : BaseFacet<GameContext>> GameEntity.whenFacetIs(fn: (GameEntity) -> Unit) {
    if (findFacet(T::class).isPresent) fn(this)
}

inline fun <reified T : BaseBehavior<GameContext>> GameEntity.hasBehavior(): Boolean {
    return findBehavior(T::class).isPresent
}
