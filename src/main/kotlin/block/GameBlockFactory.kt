package block

import entity.factories.WidgetFactory
import org.hexworks.zircon.api.data.Position3D

object GameBlockFactory {

    fun floor(position: Position3D) = GameBlock(position)

    fun wall(position: Position3D, isDiggable: Boolean = true) = GameBlock.createWith(position, WidgetFactory.newWall(isDiggable))

    fun door(position: Position3D) = GameBlock.createWith(position, WidgetFactory.newDoor())
}