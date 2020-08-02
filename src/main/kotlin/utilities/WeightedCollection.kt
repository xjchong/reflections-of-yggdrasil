package utilities

import kotlin.random.Random


data class WeightedEntry<T: Any>(val item: T, val weight: Int)

class WeightedCollection<T: Any>(vararg entries: WeightedEntry<T>) {

    private val entries = entries.toList()

    fun sample(): T? {
        var totalWeight = entries.fold(0) { acc: Int, entry: WeightedEntry<T> ->
            acc + entry.weight
        }

        val roll = Random.nextInt(totalWeight)

        for (entry in entries) {
            totalWeight -= entry.weight

            if (roll >= totalWeight) return entry.item
        }

        return null
    }
}