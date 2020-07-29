package models

data class AttackStrategy(
        val powerEfficiency: Double,
        val techEfficiency: Double,
        val criticalChance: Double,
        val criticalBonus: Double,
        val statusEffects: List<StatusEffect>
)