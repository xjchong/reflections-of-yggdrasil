package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Position3D


class Presence(var size: Int = 10) : Attribute {

    val map: HashMap<Position3D, Int> = hashMapOf()
}