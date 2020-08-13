package behaviors.aicontrollable

import attributes.Senses
import commands.Move
import considerations.Consideration
import considerations.ConsiderationContext
import entity.*
import extensions.neighbors
import game.GameContext
import models.Plan
import org.hexworks.zircon.api.data.Position3D
import utilities.DijkstraMapping

object Fleer : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: AnyEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val senses = entity.getAttribute(Senses::class) ?: return listOf()
        val sensedEnemies = senses.sensedEntities.filter { entity.isEnemiesWith(it) }
        if (sensedEnemies.isEmpty()) return listOf()

        val command = Move(context, entity) { entity.getFleePosition(context, senses) }
        val considerationContext = ConsiderationContext(context, entity)
        val plans = mutableListOf<Plan>()
        var weight = 1.0

        plans.addPlan(command, considerations, considerationContext)

        return plans
    }

    private suspend fun AnyEntity.getFleePosition(context: GameContext, senses: Senses): Position3D {
        val sensedEnemies = senses.sensedEntities.filter { isEnemiesWith(it) }
        val enemyPositions = sensedEnemies.map { it.position }.toSet()
        val avoidanceMap = DijkstraMapping.getAvoidanceMap(enemyPositions, senses.maxRange) {
            !canPass(context, it)
        }

        var nextPosition = Position3D.unknown()
        var lowestAvoidanceVal: Int? = null

        for (neighbor in position.neighbors()) {
            val avoidanceVal = avoidanceMap[neighbor] ?: continue

            if (lowestAvoidanceVal == null || avoidanceVal < lowestAvoidanceVal) {
                nextPosition = neighbor
                lowestAvoidanceVal = avoidanceVal
            }
        }

        return nextPosition
    }
}