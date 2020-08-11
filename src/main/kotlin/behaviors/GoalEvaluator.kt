package behaviors

import attributes.CombatStats
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
        val combatStats = entity.getAttribute(CombatStats::class)
        val sortedGoals = goals.list.toList().sortedBy { -it.weight }

        goals.clear()

        for (goal in sortedGoals) {
            if (goal.execute() == Consumed) {
                return true
            }
        }

        // If we didn't manage to execute any of our goals, then spend a waiting amount of time.
        entity.spendTime(EntityTime.WAIT)
        return false
    }
}