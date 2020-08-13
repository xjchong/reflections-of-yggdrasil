package behaviors.aicontrollable

import attributes.Considerations
import attributes.Plans
import behaviors.ForegroundBehavior
import commands.GameCommand
import considerations.Consideration
import considerations.ConsiderationContext
import entity.GameEntity
import entity.getAttribute
import game.GameContext
import models.Plan
import org.hexworks.amethyst.api.Attribute
import kotlin.reflect.KClass


abstract class AiControllableBehavior(vararg mandatoryAttribute: KClass<out Attribute>) :
    ForegroundBehavior(*mandatoryAttribute, Plans::class) {

    override suspend fun foregroundUpdate(entity: GameEntity, context: GameContext): Boolean {
        val plans = entity.getAttribute(Plans::class) ?: return false
        val considerations = entity.getAttribute(Considerations::class) ?: return false

        return plans.addAll(getPlans(context, entity, considerations.getConsiderationsFor(this)))
    }

    abstract suspend fun getPlans(
        context: GameContext,
        entity: GameEntity,
        considerations: List<Consideration>
    ): List<Plan>

    protected suspend fun MutableList<Plan>.addPlan(
        command: GameCommand,
        considerations: List<Consideration>,
        context: ConsiderationContext
    ) {
        var weight = 1.0

        for (consideration in considerations) {
            weight *= consideration.evaluate(context)

            if (weight <= 0.0) break
        }

        if (weight > 0.0) add(Plan(command, weight))
    }
}