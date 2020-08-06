package attributes

import models.AttackStrategy
import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Position3D
import kotlin.math.absoluteValue
import kotlin.math.max


class AttackStrategies(vararg strategies: AttackStrategy) : Attribute {

    val strategies: MutableList<AttackStrategy> = strategies.toMutableList()

    val minRange
        get() = strategies.map { it.minRange }.min() ?: 0

    val maxRange
        get() = strategies.map { it.maxRange }.max() ?: 0

    val range: IntRange
        get() = (minRange..maxRange)

    fun isInRange(attackerPos: Position3D, targetPos: Position3D): Boolean {
        return max((attackerPos.x - targetPos.x).absoluteValue, (attackerPos.y - targetPos.y).absoluteValue) in range
    }
}