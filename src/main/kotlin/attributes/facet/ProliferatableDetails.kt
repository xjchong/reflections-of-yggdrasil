package attributes.facet

import entity.GameEntity
import org.hexworks.amethyst.api.Attribute

class ProliferatableDetails(
    var factor: Double,
    var decayRate: Double,
    var proliferate: (ProliferatableDetails) -> GameEntity
) : Attribute


