package attributes

import entity.GameEntity
import entity.gameTile
import game.GameContext
import block.GameTile
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.system.Behavior
import org.hexworks.amethyst.api.system.Facet
import org.hexworks.zircon.api.data.Position3D
import kotlin.reflect.KClass

data class EntitySnapshot(val type: EntityType, val tile: GameTile)

data class Memory(val turn: Long, val strength: Double, val snapshots: List<EntitySnapshot>)

class SensoryMemory(
        private val strength: Double = 100.0, // The base number of turns before the memory fades.
        private val memorizationRate: Double = 0.0, // The rate at which memories gain strength when reinforced. 0.04 is a good value if you want this effect. 0.0 disables reinforcement.
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

    companion object {
        /**
         * As the locations are revisited, the memory of them can grow stronger.
         * The [MAX_STRENGTH_FACTOR] indicates the maximum possible strength for
         * any memory, with the max being [strength] * [MAX_STRENGTH_FACTOR].
         */
        const val MAX_STRENGTH_FACTOR: Double = 20.0
    }

    fun remember(position: Position3D, turn: Long, entities: List<GameEntity>) {
        val snapshots: MutableList<EntitySnapshot> = mutableListOf()
        val previousStrength = memories[position]?.strength ?: strength
        val nextStrength = ((1.0 - memorizationRate) * previousStrength) + (memorizationRate * strength * MAX_STRENGTH_FACTOR)

        entities.forEach {
            if (canAccept(it)) {
                snapshots.add(EntitySnapshot(it.type, it.gameTile))
            }
        }

        memories[position] = Memory(turn, nextStrength, snapshots)
    }

    fun getMemoryAt(position: Position3D): Memory? {
        return memories[position]
    }

    private fun canAccept(target: GameEntity): Boolean {
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
