package attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.Tile

data class OpenAppearance(val tile: Tile = Tile.empty()) : Attribute