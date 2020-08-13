package facets

import GameColor
import attributes.*
import commands.ApplyStatus
import commands.Attack
import commands.Destroy
import entity.AnyEntity
import entity.getAttribute
import entity.position
import entity.spendTime
import extensions.responseWhenIs
import game.GameContext
import models.AttackStrategy
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object Attackable : BaseFacet<GameContext>(CombatStats::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Attack::class) { (context, attacker, target, strategy) ->
            if (!strategy.isInRange(attacker.position, target.position)) return@responseWhenIs Pass
            val attackerStats = attacker.getAttribute(CombatStats::class) ?: return@responseWhenIs Pass
            val targetStats = target.getAttribute(CombatStats::class) ?: return@responseWhenIs Pass

            attackerStats.dockStamina(strategy.staminaCost)
            val incomingDamage = strategy.rollDamage(attackerStats)
            val finalDamage = target.applyResistances(strategy, incomingDamage)

            targetStats.run {
                dockHealth(finalDamage)

                // Update focus targets of the combatants.
                if (health > 0) {
                    attacker.getAttribute(FocusTarget::class)?.updateTarget(target)
                    attacker.getAttribute(KillTarget::class)?.target = target
                }

                target.getAttribute(KillTarget::class)?.target = attacker
                target.getAttribute(FocusTarget::class)?.run {
                    if (!this.target.isPresent) {
                        updateTarget(attacker)
                    }
                }

                context.world.observeSceneBy(
                    attacker,
                    "The $attacker ${strategy.description} the $target for ${finalDamage}!"
                )

                if (health <= 0) {
                    context.world.flash(target.position, GameColor.DESTROY_FLASH)
                    target.executeCommand(Destroy(context, target, cause = "the $attacker"))
                    attacker.getAttribute(KillTarget::class)?.target = null
                } else {
                    if (target.getAttribute(StatusDetails::class)?.guard ?: 0 > 0) {
                        context.world.flash(target.position, GameColor.GUARD_FLASH)
                    } else {
                        context.world.flash(target.position, GameColor.DAMAGE_FLASH)
                    }
                }

                for (statusEffect in strategy.statusEffects) {
                    target.executeCommand(ApplyStatus(context, attacker, target, statusEffect))
                }
            }

            attacker.spendTime(strategy.timeCost)
            Consumed
        }
    }

    private fun AnyEntity.applyResistances(strategy: AttackStrategy, incomingDamage: Int): Int {
        val innateResistances = getAttribute(Resistances::class)?.resistances?.toList() ?: listOf()
        val equippedResistances = getAttribute(Equipments::class)?.resistances()?.toList() ?: listOf()
        val applicableResistances = (innateResistances + equippedResistances).filter {
            it.type == strategy.type
        }

        val guard = getAttribute(StatusDetails::class)?.guard ?: 0
        val guardModifier = if (guard > 0) 0.25 else 1.0

        val modifiedDamage = applicableResistances.fold(incomingDamage.toDouble()) { acc, resistance ->
            acc * resistance.rollModifier
        } * guardModifier

        return modifiedDamage.toInt().coerceAtLeast(1)
    }
}