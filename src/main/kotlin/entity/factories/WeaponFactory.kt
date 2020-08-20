package entity.factories

import attributes.AttackStrategies
import attributes.EntityPosition
import attributes.EntityTile
import block.GameTile
import attributes.facet.EquippableDetails
import attributes.facet.EquippableType
import attributes.facet.OneHanded
import builders.AnyEntityBuilder
import constants.GameTileRepo
import entity.*
import facets.Droppable
import facets.Equippable
import facets.Takeable
import facets.Throwable
import models.BalancedWeaponCut
import models.TechnicalWeaponBash
import models.TechnicalWeaponCut
import models.VeryTechnicalWeaponBash
import org.hexworks.amethyst.api.entity.EntityType
import kotlin.random.Random

object WeaponFactory {

    fun newRandomWeapon(): GameEntity {
        return when (Random.nextInt(3)) {
            0 -> newRustyDagger()
            1 -> newWoodenStaff()
            else -> newIronSword()
        }
    }

    /**
     * TO BE DEPRECATED
     */

    fun newRustyDagger() = newWeaponBuilder(OneHanded, Dagger, GameTileRepo.DAGGER)
        .withAddedAttributes(AttackStrategies(TechnicalWeaponCut()))
        .withAddedFacets(Throwable)
        .build()

    fun newWoodenStaff() = newWeaponBuilder(OneHanded, Staff, GameTileRepo.STAFF)
        .withAddedAttributes(AttackStrategies(TechnicalWeaponBash()))
        .build()

    fun newIronSword() = newWeaponBuilder(OneHanded, Sword, GameTileRepo.SWORD)
        .withAddedAttributes(AttackStrategies(BalancedWeaponCut()))
        .build()

    fun newClub() = newWeaponBuilder(OneHanded, Club, GameTileRepo.CLUB)
        .withAddedAttributes(AttackStrategies(VeryTechnicalWeaponBash()))
        .build()

    /**
     * HELPERS
     */

    private fun newWeaponBuilder(equipType: EquippableType, type: EntityType, tile: GameTile) =
        AnyEntityBuilder.newBuilder(type)
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