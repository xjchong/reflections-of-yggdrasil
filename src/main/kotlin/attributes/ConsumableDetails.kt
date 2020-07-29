package attributes

import models.StatusEffect
import org.hexworks.amethyst.api.Attribute


data class ConsumableDetails(val effects: List<StatusEffect>): Attribute
