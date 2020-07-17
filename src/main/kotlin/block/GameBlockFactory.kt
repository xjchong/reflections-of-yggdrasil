package block

import entity.EntityFactory

object GameBlockFactory {

    fun floor() = GameBlock()

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

    fun door() = GameBlock.createWith(EntityFactory.newDoor())
}