package behaviors.aicontrollable

import commands.Open
import considerations.Consideration
import considerations.ConsiderationContext
import entity.AnyEntity
import entity.hasFacet
import entity.position
import extensions.neighbors
import facets.Openable
import game.GameContext
import models.Plan


object DoorOpener : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: AnyEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val neighborPositions = entity.position.neighbors()
        val openables = neighborPositions.flatMap {
            context.world.fetchEntitiesAt(it)
        }.filter {
            it.hasFacet<Openable>()
        }

        val plans = mutableListOf<Plan>()
        val considerationContext = ConsiderationContext(context, entity)

        for (openable in openables) {
            val command = Open(context, entity, openable)

            plans.addPlan(command, considerations, considerationContext)
        }

        return plans
    }
}