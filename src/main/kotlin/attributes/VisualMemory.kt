package attributes

import game.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.system.Behavior
import org.hexworks.amethyst.api.system.Facet
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import kotlin.reflect.KClass

data class EntitySnapshot(
    val type: EntityType,
    val tile: Tile?
)

class VisualMemory(
        val memory: HashMap<Position3D, List<EntitySnapshot>> = hashMapOf(),
        val requiredAttributes: Set<Attribute> = mutableSetOf(),
        val excludedAttributes: Set<Attribute> = mutableSetOf(),
        val requiredBehaviors: Set<Behavior<GameContext>> = mutableSetOf(),
        val excludedBehaviors: Set<Behavior<GameContext>> = mutableSetOf(),
        val requiredFacets: Set<Facet<GameContext>> = mutableSetOf(),
        val excludedFacets: Set<Facet<GameContext>> = mutableSetOf(),
        val requiredEntities: Set<KClass<EntityType>> = mutableSetOf(),
        val excludedEntities: Set<KClass<EntityType>> = mutableSetOf()
) : Attribute
