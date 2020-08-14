package attributes.behavior

import org.hexworks.amethyst.api.Attribute


class ShufflerDetails(var type: ShuffleBiasType = EastShuffle) : Attribute


sealed class ShuffleBiasType

object EastShuffle : ShuffleBiasType()
object SouthShuffle : ShuffleBiasType()
object WestShuffle : ShuffleBiasType()
object NorthShuffle : ShuffleBiasType()
