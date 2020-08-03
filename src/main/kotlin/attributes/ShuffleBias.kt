package attributes

import org.hexworks.amethyst.api.Attribute


class ShuffleBias(var type: ShuffleBiasType = EastShuffle) : Attribute {
}


sealed class ShuffleBiasType

object EastShuffle : ShuffleBiasType()
object SouthShuffle : ShuffleBiasType()
object WestShuffle : ShuffleBiasType()
object NorthShuffle : ShuffleBiasType()
