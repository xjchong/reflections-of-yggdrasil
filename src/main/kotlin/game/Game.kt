package game
import entity.GameEntity

class Game(val world: World,
           val player: GameEntity) {

    companion object {

        fun create(player: GameEntity, world: World) = Game(
            world = world, player = player
        )
    }
}