package attributes

import org.hexworks.amethyst.api.Attribute

class Goals : Attribute {

    val list = mutableListOf<Goal>()
    var lastGoal: Goal? = null
}

data class Goal(val name: String, val weight: Int, val execute: () -> Boolean)