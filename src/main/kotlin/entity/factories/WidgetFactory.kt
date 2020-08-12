package entity.factories

import attributes.*
import attributes.flag.IsSmellBlocking
import attributes.flag.IsBarrier
import attributes.flag.IsObstacle
import attributes.flag.IsOpaque
import builders.newGameEntityOfType
import constants.GameTile
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
                EntityTile(if (isOpen) GameTile.OPEN_DOOR else GameTile.CLOSED_DOOR),
                OpenableDetails(
                        openAppearance = GameTile.OPEN_DOOR,
                        closedAppearance = GameTile.CLOSED_DOOR,
                        isOpen = isOpen))

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
                EntityTile(GameTile.GRASS))
    }

    fun newPot() = newGameEntityOfType(Pot) {
        attributes(
                CombatStats.create(
                        1, 1, 1, 0
                ),
                EntityPosition(),
                EntityTile(GameTile.POT),
                FactionDetails(Widget),
                LootTable(
                        WeightedEntry(50) { listOf(ItemFactory.newRandomTreasure()) },
                        WeightedEntry(10) { listOf(WeaponFactory.newRandomWeapon()) },
                        WeightedEntry(10) { listOf(ArmorFactory.newRandomArmor()) },
                        WeightedEntry(30) { listOf<AnyEntity>() }
                ),
                IsObstacle)
        facets(Attackable, Destroyable)
    }

    fun newWall(isDiggable: Boolean = true) = newGameEntityOfType(Wall) {
        attributes(
                IsSmellBlocking,
                EntityPosition(),
                EntityTile(GameTile.WALL),
                IsObstacle,
                IsOpaque)
        if (isDiggable) facets(Diggable)
    }
}