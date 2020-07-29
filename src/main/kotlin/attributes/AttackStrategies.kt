package attributes

import models.AttackStrategy
import org.hexworks.amethyst.api.Attribute


class AttackStrategies(
        val strategies: MutableList<AttackStrategy> = mutableListOf()
) : Attribute {

    val maxRange: Int = 1 // TODO: Calculate this based on the current available strategies.
}


