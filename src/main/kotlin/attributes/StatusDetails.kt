package attributes

import org.hexworks.amethyst.api.Attribute


class StatusDetails : Attribute {

    var guard: Int = 0

    fun update() {
        guard = (guard - 1).coerceAtLeast(0)
    }
}