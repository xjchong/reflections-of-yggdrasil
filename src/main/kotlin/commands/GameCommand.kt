package commands

import entity.GameEntity
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.entity.EntityType


abstract class GameCommand(override val source: GameEntity) : Command<EntityType, GameContext> {

    suspend fun execute(): Response {
        return source.executeCommand(this)
    }
}
