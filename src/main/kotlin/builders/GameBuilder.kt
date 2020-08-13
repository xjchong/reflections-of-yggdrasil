package builders

import constants.GameConfig
import entity.GameEntity
import entity.factories.ArmorFactory
import entity.factories.CreatureFactory
import entity.factories.ItemFactory
import entity.factories.WeaponFactory
import events.WaitInputEvent
import game.Game
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

        addEntities(GameConfig.CREATURES_PER_LEVEL) { CreatureFactory.newRandomCreature() }
        addEntities(GameConfig.EQUIPMENT_PER_LEVEL / 2) { ArmorFactory.newRandomArmor() }
        addEntities(GameConfig.EQUIPMENT_PER_LEVEL / 2) { WeaponFactory.newRandomWeapon() }
        addEntities(GameConfig.TREASURE_PER_LEVEL) { ItemFactory.newRandomTreasure() }

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

    private fun addPlayer(): GameEntity {
        return CreatureFactory.newPlayer().addToWorld(
            atLevel = 0
        )
    }

    private fun addEntities(countPerLevel: Int, buildEntity: () -> GameEntity) {
        repeat(world.actualSize.zLength) { level ->
            repeat(countPerLevel) {
                buildEntity().addToWorld(level)
            }
        }
    }

    private fun GameEntity.addToWorld(
            atLevel: Int,
            atArea: Size = world.actualSize.to2DSize()): GameEntity {
       world.addAtEmptyPosition(this,
           offset = Position3D.defaultPosition().withZ(atLevel),
           size = Size3D.from2DSize(atArea))

        return this
    }
}