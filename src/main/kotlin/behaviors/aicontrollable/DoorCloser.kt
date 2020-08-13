package behaviors.aicontrollable

import attributes.OpenableDetails
import commands.Close
import considerations.Consideration
import considerations.ConsiderationContext
import entity.Door
import entity.GameEntity
import entity.getAttribute
import entity.position
import extensions.neighbors
import game.GameContext
import models.Plan

object DoorCloser : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: GameEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val neighborPositions = entity.position.neighbors()
        val openables = neighborPositions.flatMap {
            context.world.fetchEntitiesAt(it)
        }.filter {
            it.type == Door && it.getAttribute(OpenableDetails::class)?.isOpen == true
        }

        val plans = mutableListOf<Plan>()
        val considerationContext = ConsiderationContext(context, entity)

        for (openable in openables) {
            val command = Close(context, openable, entity)

            plans.addPlan(command, considerations, considerationContext)
        }

        return plans
    }
}