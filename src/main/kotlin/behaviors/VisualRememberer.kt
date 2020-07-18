package behaviors

import attributes.EntitySnapshot
import attributes.VisualMemory
import entity.AnyGameEntity
import entity.getAttribute
import entity.position
import entity.snapshot
import game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType

object VisualRememberer : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.inBackground) return true

        val world = context.world
        val position = entity.position
        val visualMemory = entity.getAttribute(VisualMemory::class) ?: return false

        world.findVisiblePositionsFor(entity).forEach { visiblePos ->
            world.fetchBlockAt(visiblePos).ifPresent { block ->
                val snapshots: MutableList<EntitySnapshot> = mutableListOf()

                block.entities.forEach { target ->
                    if (visualMemory.canAccept(target)) {
                        snapshots.add(target.snapshot)
                    }
                }

                visualMemory.memories[visiblePos] = snapshots
            }
        }

        return true
    }

    private fun VisualMemory.canAccept(target: AnyGameEntity): Boolean {
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