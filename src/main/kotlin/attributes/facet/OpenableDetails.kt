package attributes.facet

import block.GameTile
import org.hexworks.amethyst.api.Attribute

class OpenableDetails(
        val openAppearance: GameTile = GameTile.empty,
        val closedAppearance: GameTile = GameTile.empty,
        var isOpen: Boolean = false
) : Attribute