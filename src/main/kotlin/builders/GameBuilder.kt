package builders

import constants.GameConfig
import entity.AnyEntity
import entity.EntityFactory
import entity.GameEntity
import entity.Player
import events.WaitInputEvent
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

        addEntities(GameConfig.BATS_PER_LEVEL) { EntityFactory.newBat() }
        addEntities(GameConfig.EN_PER_LEVEL) { EntityFactory.newEn() }
        addEntities(GameConfig.FUNGI_PER_LEVEL) { EntityFactory.newFungus() }
        addEntities(GameConfig.ZOMBIES_PER_LEVEL) { EntityFactory.newZombie() }
        addEntities(GameConfig.WEAPONS_PER_LEVEL) { EntityFactory.newRandomWeapon() }
        addEntities(GameConfig.ARMOR_PER_LEVEL) { EntityFactory.newRandomArmor() }

        val game = Game.create(
            player = addPlayer(),
            world = world
        )

        world.addSceneObserver(game.player)
        world.update(WaitInputEvent()) // Update all entities to setup vision, etc. How could this be refactored?

        return game
    }

    private fun prepareWorld() = also {
        // Do things like scroll the world if necessary here.
    }

    private fun addPlayer(): GameEntity<Player> {
        return EntityFactory.newPlayer().addToWorld(
            atLevel = 0
        )
    }

    private fun addEntities(countPerLevel: Int, buildEntity: () -> AnyEntity) {
        repeat(world.actualSize.zLength) { level ->
            repeat(countPerLevel) {
                buildEntity().addToWorld(level)
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