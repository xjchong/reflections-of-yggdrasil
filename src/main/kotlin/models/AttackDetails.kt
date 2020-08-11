package models

import attributes.CombatStats

data class AttackDetails(
        val damage: Int,
        val description: String,
        val type: AttackType,
        val effects: List<StatusEffect> = listOf(),
        val timeCost: Long

) {

    companion object {
        fun create(strategy: AttackStrategy, combatStats: CombatStats): AttackDetails {
            return AttackDetails(
                    strategy.rollDamage(combatStats),
                    strategy.description,
                    strategy.type,
                    strategy.statusEffects,
                    strategy.timeCost
            )
        }
    }
}