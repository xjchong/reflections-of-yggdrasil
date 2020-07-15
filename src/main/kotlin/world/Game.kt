package world
import entity.Player
import extension.GameEntity

class Game(val world: World,
           val player: GameEntity<Player>) {

    companion object {

        fun create(player: GameEntity<Player>, world: World) = Game(
            world = world, player = player
        )
    }
}