package behaviors

import attributes.Goals
import entity.AnyEntity
import entity.getAttribute
import game.GameContext

object GoalEvaluator : ForegroundBehavior(Goals::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val goals = entity.getAttribute(Goals::class)?.list
        val sortedGoals = goals?.sortedBy { -it.weight } ?: listOf()

        goals?.clear()

        for (goal in sortedGoals) {
            if (goal.execute()) {
                return true
            }
        }

        return false
    }
}