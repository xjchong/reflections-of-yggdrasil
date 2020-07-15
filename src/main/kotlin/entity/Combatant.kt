package entity

import attribute.CombatStats
import extension.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

interface Combatant : EntityType

val GameEntity<Combatant>.combatStats: CombatStats
    get() = findAttribute(CombatStats::class).get()

fun GameEntity<Combatant>.whenDead(fn: () -> Unit) {
    if (combatStats.health <= 0) fn()
}