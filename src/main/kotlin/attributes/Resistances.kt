package attributes

import models.Resistance
import org.hexworks.amethyst.api.Attribute


class Resistances(vararg resistances: Resistance) : Attribute {

    val resistances: MutableList<Resistance> = resistances.toMutableList()
}
