package facets.active

import attributes.AttackStrategies
import attributes.CombatStats
import attributes.Equipments
import commands.Attack
import commands.AttemptAttack
import entity.AnyEntity
import entity.executeBlockingCommand
import entity.getAttribute
import entity.position
import extensions.optional
import game.GameContext
import models.AttackDetails
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
                     attackStrategy.type,
                     attackStrategy.statusEffects)

            attacker.executeBlockingCommand(Attack(context, attacker, target, attackDetails))
        }
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