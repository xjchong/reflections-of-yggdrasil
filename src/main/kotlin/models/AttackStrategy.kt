package models

import attributes.facet.AttackableDetails
import attributes.EntityTime
import org.hexworks.zircon.api.data.Position3D
import kotlin.math.absoluteValue
import kotlin.math.max

abstract class AttackStrategy(val description: String, 
                              val attackEfficiency: AttackEfficiency, 
                              val type: AttackType, val staminaCost: Int = AVERAGE_STAM_COST,
                              val timeCost: Long = EntityTime.DEFAULT) {
    
    companion object {
        const val INSUFFICIENT_STAM_PENALTY = 0.5
        const val NO_STAM_COST = 0
        const val VERY_LOW_STAM_COST = 10
        const val LOW_STAM_COST = 15
        const val AVERAGE_STAM_COST = 20
        const val HIGH_STAM_COST = 22
        const val VERY_HIGH_STAM_COST = 25
    }

    open val maxCritChance: Double = 0.5
    open val maxCritBonus: Double = 1.5
    open val statusEffects: List<StatusEffect> = listOf()
    open val minRange: Int = 1
    open val maxRange: Int = 1

    val range: IntRange
        get() = (minRange..maxRange)

    fun isInRange(attackerPos: Position3D, targetPos: Position3D): Boolean {
        return max((attackerPos.x - targetPos.x).absoluteValue, (attackerPos.y - targetPos.y).absoluteValue) in range
    }

    fun averageDamage(combatStats: AttackableDetails): Int {
        val critDamageChance = critChance(combatStats)
        val rawDamageChance = 1.0 - critDamageChance

        return ((rawDamage(combatStats) * rawDamageChance) + (critDamage(combatStats) * critDamageChance))
                .toInt()
                .coerceAtLeast(1)
    }

    fun rollDamage(combatStats: AttackableDetails): Int {
        var finalDamage = rawDamage(combatStats)

        if (Math.random() < critChance(combatStats) && combatStats.stamina > 0) {
            finalDamage = critDamage(combatStats)
        } else if (combatStats.stamina < staminaCost) {
            finalDamage *= INSUFFICIENT_STAM_PENALTY
        }

        return finalDamage.toInt().coerceAtLeast(1)
    }

    private fun rawDamage(combatStats: AttackableDetails): Double {
        val powerDamage = combatStats.power * attackEfficiency.powerEfficiency * 100
        val techDamage = combatStats.tech * attackEfficiency.techEfficiency * 100

        return powerDamage + techDamage
    }

    private fun critDamage(combatStats: AttackableDetails): Double {
        return rawDamage(combatStats) * (1.0 + ((maxCritBonus - 1.0) * combatStats.power))
    }

    private fun critChance(combatStats: AttackableDetails): Double {
        return maxCritChance * combatStats.tech
    }
}


data class SporeAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("spores", AttackEfficiency(0.1, 0.1), Bash, VERY_LOW_STAM_COST)

data class WeakClawAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("claws", AttackEfficiency(0.2, 0.3), Bash, AVERAGE_STAM_COST)

data class ClawAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("claws", AttackEfficiency(0.4, 0.6), Bash, AVERAGE_STAM_COST)

data class StrongClawAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("claws", AttackEfficiency(0.6, 0.8), Bash, HIGH_STAM_COST)

data class WeakBiteAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("bites", AttackEfficiency(0.3, 0.2), Bash, AVERAGE_STAM_COST)

data class BiteAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("bites", AttackEfficiency(0.6, 0.4), Bash, AVERAGE_STAM_COST)

data class StrongBiteAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("bites", AttackEfficiency(0.8, 0.6), Bash, HIGH_STAM_COST)

data class WeakPunchAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("punches", AttackEfficiency(0.2, 0.2), Bash, AVERAGE_STAM_COST)

data class PunchAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("punches", AttackEfficiency(0.5, 0.5), Bash, AVERAGE_STAM_COST)

data class StrongPunchAttack(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("punches", AttackEfficiency(0.7, 0.7), Bash, HIGH_STAM_COST)

data class VeryTechnicalWeaponCut(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("cuts", VeryTechnicalAttackEfficiency, Cut, VERY_LOW_STAM_COST)

data class TechnicalWeaponCut(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("slices", TechnicalAttackEfficiency, Cut, LOW_STAM_COST)

data class BalancedWeaponCut(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("slashes", BalancedAttackEfficiency, Cut, AVERAGE_STAM_COST)

data class PowerfulWeaponCut(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("cleaves", PowerfulAttackEfficiency, Cut, HIGH_STAM_COST)

data class VeryPowerfulWeaponCut(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("rends", VeryPowerfulAttackEfficiency, Cut, VERY_HIGH_STAM_COST)

data class VeryTechnicalWeaponStab(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("needles", VeryTechnicalAttackEfficiency, Stab, VERY_LOW_STAM_COST)

data class TechnicalWeaponStab(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("stabs", TechnicalAttackEfficiency, Stab, LOW_STAM_COST)

data class BalancedWeaponStab(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("pierces", BalancedAttackEfficiency, Stab, AVERAGE_STAM_COST)

data class PowerfulWeaponStab(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("gores", PowerfulAttackEfficiency, Stab, HIGH_STAM_COST)

data class VeryPowerfulWeaponStab(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("impales", VeryPowerfulAttackEfficiency, Stab, VERY_HIGH_STAM_COST)

data class VeryTechnicalWeaponBash(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("hits", VeryTechnicalAttackEfficiency, Bash, VERY_LOW_STAM_COST)

data class TechnicalWeaponBash(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("strikes", TechnicalAttackEfficiency, Bash, LOW_STAM_COST)

data class BalancedWeaponBash(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("bashes", BalancedAttackEfficiency, Bash, AVERAGE_STAM_COST)

data class PowerfulWeaponBash(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("bludgeons", PowerfulAttackEfficiency, Bash, HIGH_STAM_COST)

data class VeryPowerfulWeaponBash(override val statusEffects: List<StatusEffect> = listOf())
    : AttackStrategy("crushes", VeryPowerfulAttackEfficiency, Bash, VERY_HIGH_STAM_COST)
