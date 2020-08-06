package behaviors

import attributes.*
import commands.Attack
import entity.*
import extensions.optional
import game.GameContext
import models.AttackDetails
import models.AttackStrategy

object RandomAttacker : ForegroundBehavior(Goals::class) {

    val GOAL_KEY = "RandomAttack"

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        val attackStrategies = entity.getAttackStrategies(context)
        val inRangePositions = entity.sensedPositions.minus(entity.position).filter { sensedPos ->
            attackStrategies.isInRange(entity.position, sensedPos)
        }

        var inRangeEnemy: AnyEntity?

        for (inRangePos in inRangePositions) {
            inRangeEnemy = world.fetchEntitiesAt(inRangePos).firstOrNull {
                !entity.isAlliedWith(it)
            }

            if (inRangeEnemy != null) {
                return entity.addAttackGoal(context, inRangeEnemy, attackStrategies)
            }
        }

        return false
    }

    private fun AnyEntity.getAttackStrategies(context: GameContext): AttackStrategies {
        val innateStrategies: List<AttackStrategy> = getAttribute(AttackStrategies::class)?.strategies ?: listOf()
        val mainHand = getAttribute(Equipments::class)?.mainHand?.optional
        val mainHandStrategies: List<AttackStrategy> = mainHand?.getAttribute(AttackStrategies::class)?.strategies
                ?: listOf()

        return AttackStrategies().apply {
            strategies.addAll(innateStrategies)
            strategies.addAll(mainHandStrategies)
        }
    }

    private fun AnyEntity.pickAttackStrategy(context: GameContext, target: AnyEntity,
                                             attackStrategies: AttackStrategies): AttackStrategy? {
        return attackStrategies.strategies.filter {
            it.isInRange(position, target.position)
        }.shuffled().firstOrNull()
    }

    private fun AnyEntity.addAttackGoal(context: GameContext, target: AnyEntity, strategies: AttackStrategies): Boolean {
        val goals = getAttribute(Goals::class) ?: return false
        val combatStats = getAttribute(CombatStats::class) ?: return false
        val strategy = pickAttackStrategy(context, target, strategies) ?: return false

        combatStats.dockStamina(strategy.staminaCost)

        return goals.list.add(Goal(GOAL_KEY, 70) {
            executeBlockingCommand(Attack(context, this, target, AttackDetails.create(strategy, combatStats)))
        })
    }
}