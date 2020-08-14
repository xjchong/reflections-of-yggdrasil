package attributes.facet

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Tile

class OpenableDetails(
        val openAppearance: CharacterTile = Tile.empty(),
        val closedAppearance: CharacterTile = Tile.empty(),
        var isOpen: Boolean = false
) : Attribute