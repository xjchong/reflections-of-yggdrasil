package factory

import block.GameBlock

object GameBlockFactory {

    fun floor() = GameBlock()

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

    fun door() = GameBlock.createWith(EntityFactory.newDoor())
}