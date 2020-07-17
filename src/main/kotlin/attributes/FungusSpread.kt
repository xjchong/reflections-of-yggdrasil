package attributes

import constants.GameConfig
import org.hexworks.amethyst.api.Attribute

class FungusSpread(
    var spreadCount: Int = 0,
    val maxSpread: Int = GameConfig.FUNGI_MAX_SPREAD
) : Attribute


