package facets.active

import attributes.AttackStrategies
import attributes.CombatStats
import attributes.Equipments
import commands.Attack
import commands.AttemptAttack
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

object EquipmentFirstAttacking : BaseFacet<GameContext>(Equipments::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(AttemptAttack::class) { (context, attacker, target) ->
            val combatStats = attacker.getAttribute(CombatStats::class) ?: return@responseWhenCommandIs Pass
            val innateStrategies = attacker.getAttribute(AttackStrategies::class)?.strategies ?: mutableListOf()
            val mainHand = attacker.getAttribute(Equipments::class)?.mainHand?.optional
            val mainHandStrategies: List<AttackStrategy> =
                    mainHand?.getAttribute(AttackStrategies::class)?.strategies ?: listOf()
            val preferredAttackStrategy = mainHandStrategies.firstOrNull()
                    ?: innateStrategies.firstOrNull()
                    ?: return@responseWhenCommandIs Pass

            if (!preferredAttackStrategy.inRange(attacker.position, target.position)) return@responseWhenCommandIs Pass

            combatStats.dockStamina(preferredAttackStrategy.staminaCost)
            attacker.executeBlockingCommand(Attack(context, attacker, target, AttackDetails(
                    preferredAttackStrategy.rollDamage(combatStats),
                    preferredAttackStrategy.description,
                    preferredAttackStrategy.type,
                    preferredAttackStrategy.statusEffects
            )))
        }
    }
}