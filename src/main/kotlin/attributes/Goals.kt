package attributes

import org.hexworks.amethyst.api.Attribute

class Goals : Attribute {

    val list = mutableListOf<Goal>()
}

data class Goal(val name: String, val weight: Int, val execute: () -> Boolean)