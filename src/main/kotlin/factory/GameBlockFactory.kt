package factory

import block.GameBlock
import model.GameTileRepository

object GameBlockFactory {

    fun floor() = GameBlock(GameTileRepository.FLOOR)

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

    fun door() = GameBlock.createWith(EntityFactory.newDoor())
}