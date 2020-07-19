package entity

import attributes.EnergyLevel
import org.hexworks.amethyst.api.entity.EntityType

interface EnergyUserType : EntityType

val GameEntity<EnergyUserType>.energyLevel: EnergyLevel
    get() = findAttribute(EnergyLevel::class).get()

fun GameEntity<EnergyUserType>.whenStarved(fn: () -> Unit) {
    if (energyLevel.currentEnergy <= 0) fn()
}