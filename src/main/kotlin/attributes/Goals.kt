package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.Response

class Goals : Attribute {

    val list = mutableListOf<Goal>()
    var lastGoal: Goal? = null

    fun clear() {
        list.clear()
    }
}

data class Goal(val name: String, val weight: Int, val execute: suspend () -> Response)