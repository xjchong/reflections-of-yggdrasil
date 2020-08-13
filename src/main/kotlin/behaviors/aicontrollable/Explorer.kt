package behaviors.aicontrollable

import attributes.MoveLog
import commands.Move
import considerations.Consideration
import considerations.ConsiderationContext
import entity.AnyEntity
import entity.canPass
import entity.getAttribute
import entity.position
import extensions.neighbors
import game.GameContext
import models.Plan


object Explorer : AiControllableBehavior(MoveLog::class) {

    override suspend fun getPlans(
        context: GameContext,
        entity: AnyEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val moveLog = entity.getAttribute(MoveLog::class) ?: return listOf()
        val nextPosition = suspend {
            entity.position.neighbors().filter {
                entity.canPass(context, it)
            }.minBy {
                moveLog.visited.getOrDefault(it, 0)
            }
        }

        val command = Move(context, entity, nextPosition)
        val considerationContext = ConsiderationContext(context, entity)
        val plans = mutableListOf<Plan>()

        plans.addPlan(command, considerations, considerationContext)

        return plans
    }
}