import constants.GameConfig
import factory.WorldBuilder
import org.hexworks.zircon.api.data.Size3D

class Game(val world: World) {

    companion object {

        fun create(worldSize: Size3D = GameConfig.WORLD_SIZE,
                   visibleSize: Size3D = GameConfig.WORLD_SIZE) = Game(WorldBuilder(worldSize)
                .makeDungeon()
                .build(visibleSize))
    }
}