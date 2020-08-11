package behaviors

import attributes.EntityTime
import attributes.Goals
import entity.AnyEntity
import entity.getAttribute
import entity.spendTime
import game.GameContext
import org.hexworks.amethyst.api.Consumed

object GoalEvaluator : ForegroundBehavior(Goals::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val goals = entity.getAttribute(Goals::class) ?: return false
        val sortedGoals = goals.list.toList().sortedBy { -it.weight }

        goals.clear()

        for (goal in sortedGoals) {
            if (goal.execute() == Consumed) {
                entity.spendTime(EntityTime.DEFAULT_TIME_COST)
                return true
            }
        }

        // If we didn't manage to execute any of our goals, then spend a default amount of time.
        entity.spendTime(EntityTime.DEFAULT_TIME_COST)
        return false
    }
}