package behaviors.aicontrollable

import attributes.Inventory
import commands.Throw
import considerations.Consideration
import considerations.ConsiderationContext
import considerations.ConsiderationExtras
import entity.*
import facets.Throwable
import game.GameContext
import models.Bash
import models.Plan
import models.ThrowAttack
import org.hexworks.zircon.api.shape.LineFactory

object Thrower : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: GameEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val inventory = entity.getAttribute(Inventory::class)?.contents ?: listOf()
        val throwables = inventory.filter { it.hasFacet<Throwable>() }
        val enemies = entity.sensedEntities.filter { entity.isEnemiesWith(it) }
        val plans = mutableListOf<Plan>()

        for (enemy in enemies) {
            for (throwable in throwables) {
                val strategy = ThrowAttack(throwable, Bash)
                if (!strategy.isInRange(entity.position, enemy.position)) continue
                val path = LineFactory.buildLine(entity.position.to2DPosition(), enemy.position.to2DPosition())
                val command = Throw(context, throwable, entity, path.map { it.to3DPosition(entity.position.z) })
                val extras = ConsiderationExtras(strategy, enemy)
                val considerationContext = ConsiderationContext(context, entity, extras)

                plans.addPlan(command, considerations, considerationContext)
            }
        }

        return plans
    }
}