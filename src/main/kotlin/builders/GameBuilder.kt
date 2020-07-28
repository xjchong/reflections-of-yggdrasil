package builders

import constants.GameConfig
import entity.AnyEntity
import entity.GameEntity
import entity.Player
import entity.factories.CreatureFactory
import entity.factories.ItemFactory
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

        addEntities(GameConfig.CREATURES_PER_LEVEL) { CreatureFactory.newRandomCreature() }
        addEntities(GameConfig.EQUIPMENT_PER_LEVEL) { ItemFactory.newRandomEquipment() }
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

    private fun addPlayer(): GameEntity<Player> {
        return CreatureFactory.newPlayer().addToWorld(
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