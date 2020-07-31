package models

open class AttackEfficiency(val powerEfficiency: Double, val techEfficiency: Double)

object VeryPowerfulAttackEfficiency : AttackEfficiency(1.1, 0.3)
object PowerfulAttackEfficiency : AttackEfficiency(0.7, 0.4)
object BalancedAttackEfficiency : AttackEfficiency(0.5, 0.5)
object TechnicalAttackEfficiency : AttackEfficiency(0.3, 0.6)
object VeryTechnicalAttackEfficiency : AttackEfficiency(0.05, 0.8)