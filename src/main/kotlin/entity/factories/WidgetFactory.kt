package entity.factories

import attributes.*
import attributes.flag.BlocksSmell
import attributes.flag.Obstacle
import attributes.flag.Opaque
import behaviors.Barrier
import builders.newGameEntityOfType
import constants.GameTile
import entity.*
import facets.passive.Attackable
import facets.passive.Destroyable
import facets.passive.Diggable
import facets.passive.Openable
import utilities.WeightedEntry


object WidgetFactory {

    fun newDoor(isOpen: Boolean = false) = newGameEntityOfType(Door) {
        val baseAttributes = mutableListOf(
                EntityPosition(),
                EntityTile(if (isOpen) GameTile.OPEN_DOOR else GameTile.CLOSED_DOOR),
                OpenableDetails(
                        openAppearance = GameTile.OPEN_DOOR,
                        closedAppearance = GameTile.CLOSED_DOOR,
                        isOpen = isOpen))

        if (!isOpen) {
            baseAttributes.add(Obstacle)
            baseAttributes.add(Opaque)
            baseAttributes.add(BlocksSmell)
        }

        attributes(*baseAttributes.toTypedArray())
        behaviors(Barrier)
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
                Obstacle)
        facets(Attackable, Destroyable)
    }

    fun newWall(isDiggable: Boolean = true) = newGameEntityOfType(Wall) {
        attributes(
                BlocksSmell,
                EntityPosition(),
                EntityTile(GameTile.WALL),
                Obstacle,
                Opaque)
        if (isDiggable) facets(Diggable)
    }
}