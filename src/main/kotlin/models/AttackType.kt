package models

sealed class AttackType

object Cut : AttackType()
object Stab : AttackType()
object Bash : AttackType()