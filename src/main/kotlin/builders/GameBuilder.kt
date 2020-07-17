package builders

import constants.GameConfig
import entity.EntityFactory
import entity.Player
import entity.GameEntity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Size3D
import game.Game

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

    private fun addFungi() = also {
        repeat(world.actualSize.zLength) {level ->
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