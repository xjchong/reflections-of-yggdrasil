package behaviors

import attributes.facet.ProliferatableDetails
import behaviors.aicontrollable.AiControllableBehavior
import commands.Proliferate
import considerations.Consideration
import considerations.ConsiderationContext
import entity.GameEntity
import game.GameContext
import models.Plan

object Proliferator : AiControllableBehavior(ProliferatableDetails::class) {

    override suspend fun getPlans(
        context: GameContext,
        entity: GameEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val command = Proliferate(context, entity)
        val plans = mutableListOf<Plan>()

        plans.addPlan(command, considerations, ConsiderationContext(context, entity))

        return plans
    }
}