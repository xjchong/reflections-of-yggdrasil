package attributes

import entity.GameEntity
import org.hexworks.amethyst.api.Attribute
import utilities.WeightedCollection
import utilities.WeightedEntry


class LootTable(vararg weightedEntries: WeightedEntry<() -> List<GameEntity>>) : Attribute {

    val table: WeightedCollection<() -> List<GameEntity>> = WeightedCollection(*weightedEntries)
}
