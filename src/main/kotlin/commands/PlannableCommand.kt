package commands

import entity.AnyEntity
import entity.spendTime
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.entity.EntityType


abstract class PlannableCommand(private val executor: AnyEntity, private val timeCost: Long) : GameCommand<EntityType> {

    suspend fun execute(): Response {
        return if (executor.executeCommand(this) == Consumed) {
            source.spendTime(timeCost)
            Consumed
        } else Pass
    }
}


