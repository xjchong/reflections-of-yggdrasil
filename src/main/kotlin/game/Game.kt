package game
import entity.Player
import entity.GameEntity

class Game(val world: World,
           val player: GameEntity<Player>) {

    companion object {

        fun create(player: GameEntity<Player>, world: World) = Game(
            world = world, player = player
        )
    }
}