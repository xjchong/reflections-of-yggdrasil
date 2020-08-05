package behaviors

import attributes.AttackStrategies
import attributes.Goal
import attributes.Goals
import commands.AttemptAttack
import entity.*
import game.GameContext
import org.hexworks.amethyst.api.Consumed
import kotlin.math.absoluteValue

object Attacker : ForegroundBehavior(Goals::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        val attackStrategies = entity.getAttribute(AttackStrategies::class)
        val attackRange = ((attackStrategies?.minRange ?: 0)..(attackStrategies?.maxRange ?: 0))
        val inRangePositions = entity.sensedPositions.minus(entity.position).filter { sensedPos ->
            (sensedPos.x - entity.position.x).absoluteValue in attackRange ||
                    (sensedPos.y - entity.position.y).absoluteValue in attackRange
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
        return getAttribute(Goals::class)?.list?.add(Goal("AttemptAttack",100) {
            executeBlockingCommand(AttemptAttack(context, this, target)) == Consumed
        }) == true
    }
}