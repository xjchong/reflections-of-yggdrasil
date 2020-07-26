package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property


class Vigilance(
        private val maxAlertLevelProp: Property<Int>,
        private val alertLevelProp: Property<Int>) : Attribute {
    val maxAlertLevel: Int by maxAlertLevelProp.asDelegate()
    val alertLevel: Int by alertLevelProp.asDelegate()

    companion object {

        fun create(maxAlertLevel: Int, alertLevel: Int = 0) =
                Vigilance(
                        maxAlertLevelProp = createPropertyFrom(maxAlertLevel),
                        alertLevelProp = createPropertyFrom(alertLevel)
                )
    }

    fun relax() {
        val newAlertLevel = (alertLevel - 1).coerceAtLeast(0)
        alertLevelProp.updateValue(newAlertLevel)
    }

    fun alert() {
        alertLevelProp.updateValue(maxAlertLevel)
    }
}