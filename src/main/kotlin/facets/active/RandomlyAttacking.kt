package facets.active

import attributes.AttackStrategies
import attributes.CombatStats
import attributes.Equipments
import commands.Attack
import models.AttackDetails
import commands.AttemptAttack
import entity.AnyEntity
import entity.executeBlockingCommand
import entity.getAttribute
import entity.position
import extensions.optional
import game.GameContext
import models.AttackStrategy
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import kotlin.math.absoluteValue
import kotlin.math.max


object RandomlyAttacking : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(AttemptAttack::class) { (context, attacker, target) ->
            val attackStrategies = attacker.getAttackStrategies(context)
            val attackStrategy = attacker.pickAttackStrategy(context, target, attackStrategies)
                    ?: return@responseWhenCommandIs Pass

            val attackerCombatStats = attacker.getAttribute(CombatStats::class) ?: return@responseWhenCommandIs Pass
            attackerCombatStats.dockStamina(attackStrategy.staminaCost)

             val attackDetails = AttackDetails(
                     attackStrategy.rollDamage(attackerCombatStats),
                     attackStrategy.description,
                     attackStrategy.type)

            attacker.executeBlockingCommand(Attack(context, attacker, target, attackDetails))
        }
    }

    private fun AnyEntity.getAttackStrategies(context: GameContext): AttackStrategies {
        val innateStrategies = getAttribute(AttackStrategies::class)?.strategies ?: arrayOf()
        val mainHand = getAttribute(Equipments::class)?.mainHand?.optional
        val equipmentStrategies = mainHand?.getAttribute(AttackStrategies::class)?.strategies ?: arrayOf()

        return AttackStrategies(*innateStrategies, *equipmentStrategies)
    }

    private fun AnyEntity.pickAttackStrategy(context: GameContext, target: AnyEntity,
                                             attackStrategies: AttackStrategies): AttackStrategy? {
        val attackerPos = position
        val targetPos = target.position
        val currentRange = max((attackerPos.x - targetPos.x).absoluteValue, (attackerPos.y - targetPos.y).absoluteValue)

        val viableStrategies = attackStrategies.strategies.filter {
            it.minRange <= currentRange && it.maxRange >= currentRange
        }

        return if (viableStrategies.isNotEmpty()) {
            viableStrategies.random()
        } else null
    }
}