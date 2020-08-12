package behaviors.aicontrollable

import attributes.AttackStrategies
import attributes.Equipments
import attributes.Senses
import commands.Attack
import considerations.Consideration
import considerations.ConsiderationContext
import considerations.ConsiderationExtras
import entity.AnyEntity
import entity.getAttribute
import entity.isEnemiesWith
import extensions.optional
import game.GameContext
import models.Plan

object Attacker : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: AnyEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val senses = entity.getAttribute(Senses::class) ?: return listOf()
        val enemies = senses.sensedEntities.filter { entity.isEnemiesWith(it) }
        val innateAttackStrategies = entity.getAttribute(AttackStrategies::class)?.strategies?.toList() ?: listOf()
        val mainHand = entity.getAttribute(Equipments::class)?.mainHand?.optional
        val mainHandAttackStrategies = mainHand?.getAttribute(AttackStrategies::class)?.strategies?.toList() ?: listOf()
        val allAttackStrategies = innateAttackStrategies + mainHandAttackStrategies
        val plans = mutableListOf<Plan>()

        for (strategy in allAttackStrategies) {
            for (enemy in enemies) {
                val command = Attack(context, entity, enemy, strategy)
                val considerationExtras = ConsiderationExtras(target = enemy, attackStrategy = strategy)
                val considerationContext = ConsiderationContext(context, entity, considerationExtras)

                plans.addPlan(command, considerations, considerationContext)
            }
        }

        return plans
    }
}