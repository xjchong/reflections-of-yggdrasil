package builders

import constants.GameConfig
import entity.EntityFactory
import entity.GameEntity
import entity.Player
import game.Game
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Size3D

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
        addBats()
        addEn()
        addFungi()

        val game = Game.create(
            player = addPlayer(),
            world = world
        )

        world.addWorldEntity(EntityFactory.newFogOfWar(game))

        return game
    }

    private fun prepareWorld() = also {
        // Do things like scroll the world if necessary here.
    }

    private fun addPlayer(): GameEntity<Player> {
        return EntityFactory.newPlayer().addToWorld(
            atLevel = GameConfig.DUNGEON_LEVEL_COUNT - 1
        )
    }

    private fun addBats() = also {
        repeat(world.actualSize.zLength) { level ->
            repeat(GameConfig.BATS_PER_LEVEL) {
                EntityFactory.newBat().addToWorld(level)
            }
        }
    }

    private fun addEn() = also {
        repeat(world.actualSize.zLength) { level ->
            repeat(GameConfig.EN_PER_LEVEL) {
                EntityFactory.newEn().addToWorld(level)
            }
        }
    }

    private fun addFungi() = also {
        repeat(world.actualSize.zLength) { level ->
            repeat(GameConfig.FUNGI_PER_LEVEL) {
                EntityFactory.newFungus().addToWorld(level)
            }
        }
    }

    private fun <T : EntityType> GameEntity<T>.addToWorld(
            atLevel: Int,
            atArea: Size = world.actualSize.to2DSize()): GameEntity<T> {
       world.addAtEmptyPosition(this,
           offset = Position3D.defaultPosition().withZ(atLevel),
           size = Size3D.from2DSize(atArea))

        return this
    }
}