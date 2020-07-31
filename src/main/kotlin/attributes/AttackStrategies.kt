package attributes

import models.AttackStrategy
import org.hexworks.amethyst.api.Attribute


class AttackStrategies(vararg strategies: AttackStrategy) : Attribute {

    val strategies: MutableList<AttackStrategy> = strategies.toMutableList()

    val minRange
        get() = strategies.map { it.minRange }.min() ?: 0

    val maxRange
        get() = strategies.map { it.maxRange }.max() ?: 0
}