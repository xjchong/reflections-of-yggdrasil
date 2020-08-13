package commands

import entity.AnyEntity
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.entity.EntityType


abstract class PlannableCommand(private val executor: AnyEntity) : GameCommand<EntityType> {

    suspend fun execute(): Response {
        return executor.executeCommand(this)
    }
}


