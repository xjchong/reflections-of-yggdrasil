package models

import attributes.CombatStats

interface AttackStrategy {

    val description: String
    val powerEfficiency: Double
    val techEfficiency: Double
    val maxCritChance: Double
    val maxCritBonus: Double
    val staminaCost: Int
    val statusEffects: List<StatusEffect>
    val minRange: Int
    val maxRange: Int

    fun averageDamage(combatStats: CombatStats): Int {
        val critDamageChance = critChance(combatStats)
        val rawDamageChance = 1.0 - critDamageChance

        return ((rawDamage(combatStats) * rawDamageChance) + (critDamage(combatStats) * critDamageChance))
                .toInt()
                .coerceAtLeast(1)
    }

    fun rollDamage(combatStats: CombatStats): Int {
        var finalDamage = rawDamage(combatStats)

        if (Math.random() < critChance(combatStats) && combatStats.stamina > 0) {
            finalDamage = critDamage(combatStats)
        }

        combatStats.dockStamina(staminaCost)

        return finalDamage.toInt().coerceAtLeast(1)
    }

    private fun rawDamage(combatStats: CombatStats): Double {
        val powerDamage = combatStats.power * powerEfficiency * 100
        val techDamage = combatStats.skill * techEfficiency * 100

        return powerDamage + techDamage
    }

    private fun critDamage(combatStats: CombatStats): Double {
        return rawDamage(combatStats) * (1.0 + ((maxCritBonus - 1.0) * combatStats.power))
    }

    private fun critChance(combatStats: CombatStats): Double {
        return maxCritChance * combatStats.skill
    }
}


data class HitAttack(override val powerEfficiency: Double = 1.0, override val techEfficiency: Double = 1.0) : AttackStrategy {

    override val description: String = "hits"
    override val maxCritChance: Double = 0.5
    override val maxCritBonus: Double = 1.5
    override val staminaCost: Int = 20
    override val statusEffects: List<StatusEffect> = listOf()
    override val maxRange: Int = 1
    override val minRange: Int = 1
}