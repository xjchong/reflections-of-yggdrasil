package entity.factories

import attributes.*
import attributes.facet.AttackableDetails
import attributes.facet.OpenableDetails
import attributes.flag.*
import builders.newGameEntityOfType
import constants.GameTileRepo
import entity.*
import facets.Attackable
import facets.Destroyable
import facets.Diggable
import facets.Openable
import utilities.WeightedEntry


object WidgetFactory {

    fun newDoor(isOpen: Boolean = false) = newGameEntityOfType(Door) {
        val baseAttributes = mutableListOf(
                IsBarrier,
                EntityPosition(),
                EntityTile(if (isOpen) GameTileRepo.OPEN_DOOR else GameTileRepo.CLOSED_DOOR),
            OpenableDetails(
                openAppearance = GameTileRepo.OPEN_DOOR,
                closedAppearance = GameTileRepo.CLOSED_DOOR,
                isOpen = isOpen
            )
        )

        if (!isOpen) {
            baseAttributes.add(IsObstacle)
            baseAttributes.add(IsOpaque)
            baseAttributes.add(IsSmellBlocking)
        }

        attributes(*baseAttributes.toTypedArray())
        facets(Openable)
    }

    fun newGrass() = newGameEntityOfType(Grass) {
        attributes(
                EntityPosition(),
                EntityTile(GameTileRepo.GRASS),
                IsTerrain
        )
    }

    fun newPot() = newGameEntityOfType(Pot) {
        attributes(
                AttackableDetails.create(
                        1, 1, 1, 0
                ),
                EntityPosition(),
                EntityTile(GameTileRepo.POT),
                Factions(Widget),
                LootTable(
                        WeightedEntry(50) { listOf(ItemFactory.newRandomTreasure()) },
                        WeightedEntry(10) { listOf(WeaponFactory.newRandomWeapon()) },
                        WeightedEntry(10) { listOf(ArmorFactory.newRandomArmor()) },
                        WeightedEntry(30) { listOf<GameEntity>() }
                ),
                IsObstacle)
        facets(Attackable, Destroyable)
    }

    fun newWall(isDiggable: Boolean = true) = newGameEntityOfType(Wall) {
        attributes(
                IsSmellBlocking,
                EntityPosition(),
                EntityTile(GameTileRepo.WALL),
                IsObstacle,
                IsOpaque)
        if (isDiggable) facets(Diggable)
    }
}