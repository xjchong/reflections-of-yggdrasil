package attributes

import models.Resistance
import org.hexworks.amethyst.api.Attribute


class Resistances(
        val resistances: MutableList<Resistance> = mutableListOf()
) : Attribute
