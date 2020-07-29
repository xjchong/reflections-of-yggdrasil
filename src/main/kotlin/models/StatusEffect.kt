package models


data class StatusEffect(
        val type: StatusEffectType,
        val potency: Int,
        val chance: Double = 1.0
)


sealed class StatusEffectType

object Heal : StatusEffectType()
object Poison : StatusEffectType() { const val damage = 5 }