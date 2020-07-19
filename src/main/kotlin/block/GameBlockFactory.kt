package block

import entity.EntityFactory

object GameBlockFactory {

    fun floor() = GameBlock()

    fun wall(isDiggable: Boolean = true) = GameBlock.createWith(EntityFactory.newWall(isDiggable))

    fun door() = GameBlock.createWith(EntityFactory.newDoor())
}