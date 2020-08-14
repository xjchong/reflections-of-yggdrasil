package entity.factories

import attributes.*
import attributes.facet.EquippableDetails
import attributes.facet.EquippableType
import attributes.facet.OneHanded
import builders.AnyEntityBuilder
import constants.GameTile
import entity.*
import facets.Droppable
import facets.Equippable
import facets.Takeable
import models.BalancedWeaponCut
import models.TechnicalWeaponBash
import models.TechnicalWeaponCut
import models.VeryTechnicalWeaponBash
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.CharacterTile
import kotlin.random.Random

object WeaponFactory {

    fun newRandomWeapon(): GameEntity {
        return when(Random.nextInt(3)) {
            0 -> newRustyDagger()
            1 -> newWoodenStaff()
            else -> newIronSword()
        }
    }

    /**
     * TO BE DEPRECATED
     */

    fun newRustyDagger() = newWeaponBuilder(OneHanded, Dagger, GameTile.DAGGER)
            .withAddedAttributes(AttackStrategies(TechnicalWeaponCut()))
            .build()

    fun newWoodenStaff() = newWeaponBuilder(OneHanded, Staff, GameTile.STAFF)
            .withAddedAttributes(AttackStrategies(TechnicalWeaponBash()))
            .build()

    fun newIronSword() = newWeaponBuilder(OneHanded, Sword, GameTile.SWORD)
            .withAddedAttributes(AttackStrategies(BalancedWeaponCut()))
            .build()

    fun newClub() = newWeaponBuilder(OneHanded, Club, GameTile.CLUB)
            .withAddedAttributes(AttackStrategies(VeryTechnicalWeaponBash()))
            .build()

    /**
     * HELPERS
     */

    private fun newWeaponBuilder(equipType: EquippableType, type: EntityType, tile: CharacterTile) = AnyEntityBuilder.newBuilder(type)
            .withAttributes(
                    EntityTile(tile),
                    EntityPosition(),
                EquippableDetails(equipType)
            )
            .withFacets(
                Droppable,
                Takeable,
                Equippable
            )
}