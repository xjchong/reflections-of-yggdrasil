package attributes

import block.GameTile
import org.hexworks.amethyst.api.Attribute


class EntityTile(
        var gameTile: GameTile = GameTile.empty
) : Attribute