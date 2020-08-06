package block

import entity.factories.WidgetFactory
import org.hexworks.zircon.api.data.Position3D

object GameBlockFactory {

    fun door(position: Position3D, isOpen: Boolean = false) = GameBlock.createWith(position, WidgetFactory.newDoor(isOpen))

    fun floor(position: Position3D) = GameBlock(position)

    fun wall(position: Position3D, isDiggable: Boolean = true) = GameBlock.createWith(position, WidgetFactory.newWall(isDiggable))
}