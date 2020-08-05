package behaviors

import attributes.AttackStrategies
import attributes.Goal
import attributes.Goals
import commands.AttemptAttack
import entity.*
import game.GameContext
import org.hexworks.amethyst.api.Consumed
import kotlin.math.absoluteValue
import kotlin.math.max

object Attacker : ForegroundBehavior(Goals::class) {

    val GOAL_KEY = "AttemptAttack"

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        val attackStrategies = entity.getAttribute(AttackStrategies::class)
        val attackRange = ((attackStrategies?.minRange ?: 0)..(attackStrategies?.maxRange ?: 0))
        val inRangePositions = entity.sensedPositions.minus(entity.position).filter { sensedPos ->
            max((sensedPos.x - entity.position.x).absoluteValue, (sensedPos.y - entity.position.y).absoluteValue) in attackRange
        }

        var inRangeEnemy: AnyEntity?

        for (inRangePos in inRangePositions) {
            inRangeEnemy = world.fetchEntitiesAt(inRangePos).firstOrNull {
                !entity.isAlliedWith(it)
            }

            if (inRangeEnemy != null) {
                return entity.addAttemptAttackGoal(context, inRangeEnemy)
            }
        }

        return false
    }

    private fun AnyEntity.addAttemptAttackGoal(context: GameContext, target: AnyEntity): Boolean {
        return getAttribute(Goals::class)?.list?.add(Goal(GOAL_KEY,70) {
            executeBlockingCommand(AttemptAttack(context, this, target)) == Consumed
        }) == true
    }
}