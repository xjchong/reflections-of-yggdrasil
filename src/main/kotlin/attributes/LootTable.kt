package attributes

import entity.AnyEntity
import org.hexworks.amethyst.api.Attribute
import utilities.WeightedCollection
import utilities.WeightedEntry


class LootTable(vararg weightedEntries: WeightedEntry<() -> List<AnyEntity>>) : Attribute {

    val table: WeightedCollection<() -> List<AnyEntity>> = WeightedCollection(*weightedEntries)
}
