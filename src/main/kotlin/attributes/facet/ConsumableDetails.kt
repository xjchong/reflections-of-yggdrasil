package attributes.facet

import models.StatusEffect
import org.hexworks.amethyst.api.Attribute


class ConsumableDetails(vararg effects: StatusEffect): Attribute {

    val effects: List<StatusEffect> = effects.toList()
}
