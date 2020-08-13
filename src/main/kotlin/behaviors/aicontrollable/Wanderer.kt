package behaviors.aicontrollable

import commands.Move
import considerations.Consideration
import considerations.ConsiderationContext
import entity.AnyEntity
import entity.canPass
import entity.position
import extensions.neighbors
import game.GameContext
import models.Plan

object Wanderer : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: AnyEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val considerationContext = ConsiderationContext(context, entity)
        val command = Move(context, entity) {
            entity.position.neighbors().first { entity.canPass(context, it) }
        }

        val plans = mutableListOf<Plan>()

        plans.addPlan(command, considerations, considerationContext)

        return plans
    }
}