package models


data class Resistance(val type: Any, val chance: Double, val modifier: Double) {

    val rollModifier: Double
        get() = if (Math.random() < chance) modifier else 1.0
}
