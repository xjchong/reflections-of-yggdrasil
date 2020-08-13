package facets

import attributes.Considerations
import attributes.EntityTime
import attributes.Plans
import commands.ExecuteAiPlans
import entity.GameEntity
import entity.getAttribute
import entity.spendTime
import extensions.responseWhenIs
import game.GameContext
import models.Plan
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object AiControllable : BaseFacet<GameContext>(Plans::class, Considerations::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(ExecuteAiPlans::class) { (context, entity) ->
            if (context.inBackground) return@responseWhenIs Pass
            val plans = entity.getAttribute(Plans::class) ?: return@responseWhenIs Pass
            val response = entity.attemptPlan(plans.list)

            plans.clear()

            response
        }
    }

    private suspend fun GameEntity.attemptPlan(plans: List<Plan>): Response {
        val sortedPlans = sortPlans(plans)
        for (plan in sortedPlans) {
            if (plan.command.execute() == Consumed) return Consumed
        }

        spendTime(EntityTime.WAIT)

        return Pass
    }

    private suspend fun sortPlans(plans: List<Plan>): List<Plan> {
        return plans.shuffled().sortedBy { -it.weight }
    }
}