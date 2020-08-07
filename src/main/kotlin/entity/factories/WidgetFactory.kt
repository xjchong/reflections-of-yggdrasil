package entity.factories

import attributes.EntityPosition
import attributes.EntityTile
import attributes.OpenableDetails
import attributes.flag.BlocksSmell
import attributes.flag.Obstacle
import attributes.flag.Opaque
import behaviors.Barrier
import builders.newGameEntityOfType
import constants.GameTile
import entity.Door
import entity.Grass
import entity.Pot
import entity.Wall
import facets.passive.Diggable
import facets.passive.Openable


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
                EntityPosition(),
                EntityTile(GameTile.POT),
                Obstacle)
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