package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile

data class EntitySnapshot(
    val type: EntityType,
    val tile: Tile?
)

class VisualMemory(
    val memory: HashMap<Position3D, List<EntitySnapshot>> = hashMapOf()
) : Attribute