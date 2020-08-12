package behaviors.aicontrollable

import attributes.Senses
import commands.Move
import considerations.Consideration
import considerations.ConsiderationContext
import considerations.ConsiderationExtras
import entity.AnyEntity
import entity.getAttribute
import entity.isEnemiesWith
import entity.position
import game.GameContext
import models.Plan
import utilities.AStar

object Chaser : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: AnyEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val senses = entity.getAttribute(Senses::class) ?: return listOf()
        val enemies = senses.sensedEntities.filter { entity.isEnemiesWith(it) }
        val plans = mutableListOf<Plan>()

        for (enemy in enemies) {
            val considerationExtras = ConsiderationExtras(target = enemy)
            val considerationContext = ConsiderationContext(context, entity, considerationExtras)
            val command = Move(context, entity) {
                AStar.getPath(entity.position, enemy.position) { from, to ->
                    context.world.getMovementCost(entity, from, to)
                }.first()
            }

            plans.addPlan(command, considerations, considerationContext)
        }

        return plans
    }
}