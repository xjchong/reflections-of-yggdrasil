package extensions

import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property


fun Property<Int>.toStringProperty(transform: (Int) -> String = { it.toString() }): Property<String> {
    val intProp = this

    return createPropertyFrom("").apply {
        this.updateFrom(intProp) {
            transform(it)
        }
    }
}
