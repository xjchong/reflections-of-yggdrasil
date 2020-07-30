package models

data class AttackDetails(
        val damage: Int,
        val description: String,
        val type: AttackType,
        val effects: List<StatusEffect> = listOf()
)