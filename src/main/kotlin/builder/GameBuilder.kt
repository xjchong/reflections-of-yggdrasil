package builder

import constants.GameConfig
import entity.Player
import extension.GameEntity
import factory.EntityFactory
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import world.Game

class GameBuilder(val worldSize: Size3D) {

    companion object {
        fun defaultGame() = GameBuilder(worldSize = GameConfig.WORLD_SIZE)
            .buildGame()
    }

    val world = WorldBuilder(worldSize)
        .makeDungeon()
        .build(visibleSize = GameConfig.WORLD_SIZE)

    fun buildGame(): Game {
        prepareWorld()

        return Game.create(
            player = addPlayer(),
            world = world
        )
    }

    private fun prepareWorld() = also {
        // Do things like scroll the world if necessary here.
    }

    private fun addPlayer(): GameEntity<Player> {
        val player = EntityFactory.newPlayer()

        world.addAtEmptyPosition(
            player,
            offset = Position3D.defaultPosition().withZ(GameConfig.DUNGEON_LEVEL_COUNT - 1),
            size = world.visibleSize.copy(zLength =  0))

        return player
    }
}