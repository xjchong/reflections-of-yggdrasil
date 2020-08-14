package behaviors.aicontrollable

import attributes.facet.OpenableDetails
import commands.Open
import considerations.Consideration
import considerations.ConsiderationContext
import entity.Door
import entity.GameEntity
import entity.getAttribute
import entity.position
import extensions.neighbors
import game.GameContext
import models.Plan


object DoorOpener : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: GameEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val neighborPositions = entity.position.neighbors()
        val openables = neighborPositions.flatMap {
            context.world.fetchEntitiesAt(it)
        }.filter {
            it.type == Door && it.getAttribute(OpenableDetails::class)?.isOpen == false
        }

        val plans = mutableListOf<Plan>()
        val considerationContext = ConsiderationContext(context, entity)

        for (openable in openables) {
            val command = Open(context, openable, entity)

            plans.addPlan(command, considerations, considerationContext)
        }

        return plans
    }
}