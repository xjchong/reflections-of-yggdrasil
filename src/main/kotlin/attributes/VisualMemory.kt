package attributes

import entity.AnyGameEntity
import entity.tile
import game.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.system.Behavior
import org.hexworks.amethyst.api.system.Facet
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position3D
import kotlin.reflect.KClass

data class EntitySnapshot(val type: EntityType, val tile: CharacterTile)

data class Memory(val turn: Int, val strength: Int, val snapshots: List<EntitySnapshot>)

class VisualMemory(
        val strength: Int,
        private val memories: HashMap<Position3D, Memory> = hashMapOf(),
        private val timestamps: HashMap<Position3D, Int> = hashMapOf(),
        private val requiredAttributes: Set<Attribute> = mutableSetOf(),
        private val excludedAttributes: Set<Attribute> = mutableSetOf(),
        private val requiredBehaviors: Set<Behavior<GameContext>> = mutableSetOf(),
        private val excludedBehaviors: Set<Behavior<GameContext>> = mutableSetOf(),
        private val requiredFacets: Set<Facet<GameContext>> = mutableSetOf(),
        private val excludedFacets: Set<Facet<GameContext>> = mutableSetOf(),
        private val requiredEntityTypes: Set<KClass<EntityType>> = mutableSetOf(),
        private val excludedEntityTypes: Set<KClass<EntityType>> = mutableSetOf()) : Attribute {

    fun remember(position: Position3D, turn: Int, entities: List<AnyGameEntity>) {
        val snapshots: MutableList<EntitySnapshot> = mutableListOf()

        entities.forEach {
            if (canAccept(it)) {
                snapshots.add(EntitySnapshot(it.type, it.tile))
            }
        }

        memories[position] = Memory(turn, strength, snapshots)
    }

    fun getMemoryAt(position: Position3D): Memory? {
        return memories[position]
    }

    private fun canAccept(target: AnyGameEntity): Boolean {
        if (excludedAttributes.isNotEmpty() || requiredAttributes.isNotEmpty()) {
            val targetAttributes = target.attributes.toSet()

            if (excludedAttributes.intersect(targetAttributes).isNotEmpty()) return false
            if (requiredAttributes.isNotEmpty()
                    && requiredAttributes.intersect(targetAttributes).isEmpty()) return false
        }

        if (excludedBehaviors.isNotEmpty() || requiredBehaviors.isNotEmpty()) {
            val targetBehaviors = target.behaviors.toSet()

            if (excludedBehaviors.intersect(targetBehaviors).isNotEmpty()) return false
            if (requiredBehaviors.isNotEmpty()
                    && requiredBehaviors.intersect(targetBehaviors).isEmpty()) return false
        }

        if (excludedFacets.isNotEmpty() || requiredFacets.isNotEmpty()) {
            val targetFacets = target.facets.toSet()

            if (excludedFacets.intersect(targetFacets).isNotEmpty()) return false
            if (requiredFacets.isNotEmpty()
                    && requiredFacets.intersect(targetFacets).isEmpty()) return false
        }

        if (excludedEntityTypes.isNotEmpty()
                && excludedEntityTypes.contains(target.type::class)) return false

        if (requiredEntityTypes.isNotEmpty()
                && requiredEntityTypes.contains(target.type::class).not()) return false

        return true
    }
}
