package entity.factories

import attributes.EntityPosition
import attributes.EntityTile
import attributes.OpenAppearance
import attributes.flag.BlocksSmell
import attributes.flag.Obstacle
import attributes.flag.Opaque
import attributes.flag.Opened
import behaviors.Barrier
import builders.newGameEntityOfType
import constants.GameTile
import entity.Door
import entity.Wall
import facets.passive.Diggable
import facets.passive.Openable


object WidgetFactory {

    fun newWall(isDiggable: Boolean = true) = newGameEntityOfType(Wall) {
        attributes(
                BlocksSmell,
                EntityPosition(),
                EntityTile(GameTile.WALL),
                Obstacle,
                Opaque)
        if (isDiggable) facets(Diggable)
    }

    fun newDoor(isOpened: Boolean = false) = newGameEntityOfType(Door) {
        val baseAttributes = mutableListOf(
                EntityPosition(),
                EntityTile(GameTile.DOOR),
                OpenAppearance(GameTile.DOOR.withCharacter('\'')))

        if (isOpened) {
            baseAttributes.add(Opened)
        } else {
            baseAttributes.add(Obstacle)
            baseAttributes.add(Opaque)
            baseAttributes.add(BlocksSmell)
        }

        attributes(*baseAttributes.toTypedArray())
        behaviors(Barrier)
        facets(Openable)
    }
}