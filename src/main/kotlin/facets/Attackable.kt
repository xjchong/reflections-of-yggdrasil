package facets

import GameColor
import attributes.Equipments
import attributes.FocusTarget
import attributes.KillTarget
import attributes.Resistances
import attributes.facet.AttackableDetails
import attributes.facet.StatusApplicableDetails
import commands.ApplyStatus
import commands.Attack
import commands.Destroy
import entity.GameEntity
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


object Attackable : BaseFacet<GameContext>(AttackableDetails::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Attack::class) { (context, attackable, attacker, strategy) ->
            if (!strategy.isInRange(attacker.position, attackable.position)) return@responseWhenIs Pass
            val attackerStats = attacker.getAttribute(AttackableDetails::class) ?: return@responseWhenIs Pass
            val attackableStats = attackable.getAttribute(AttackableDetails::class) ?: return@responseWhenIs Pass

            attackerStats.dockStamina(strategy.staminaCost)
            val incomingDamage = strategy.rollDamage(attackerStats)
            val finalDamage = attackable.applyResistances(strategy, incomingDamage)

            attackableStats.run {
                dockHealth(finalDamage)

                // Update focus targets of the combatants.
                if (health > 0) {
                    attacker.getAttribute(FocusTarget::class)?.updateTarget(attackable)
                    attacker.getAttribute(KillTarget::class)?.target = attackable
                }

                attackable.getAttribute(KillTarget::class)?.target = attacker
                attackable.getAttribute(FocusTarget::class)?.run {
                    if (!this.target.isPresent) {
                        updateTarget(attacker)
                    }
                }

                context.world.observeSceneBy(
                    attacker,
                    "The $attacker ${strategy.description} the $attackable for ${finalDamage}!"
                )

                if (health <= 0) {
                    context.world.flash(attackable.position, GameColor.DESTROY_FLASH)
                    attackable.executeCommand(Destroy(context, attackable, cause = "the $attacker"))
                    attacker.getAttribute(KillTarget::class)?.target = null
                } else {
                    if (attackable.getAttribute(StatusApplicableDetails::class)?.guard ?: 0 > 0) {
                        context.world.flash(attackable.position, GameColor.GUARD_FLASH)
                    } else {
                        context.world.flash(attackable.position, GameColor.DAMAGE_FLASH)
                    }
                }

                for (statusEffect in strategy.statusEffects) {
                    attackable.executeCommand(ApplyStatus(context, attacker, attackable, statusEffect))
                }
            }

            attacker.spendTime(strategy.timeCost)
            Consumed
        }
    }

    private fun GameEntity.applyResistances(strategy: AttackStrategy, incomingDamage: Int): Int {
        val innateResistances = getAttribute(Resistances::class)?.resistances?.toList() ?: listOf()
        val equippedResistances = getAttribute(Equipments::class)?.resistances()?.toList() ?: listOf()
        val applicableResistances = (innateResistances + equippedResistances).filter {
            it.type == strategy.type
        }

        val guard = getAttribute(StatusApplicableDetails::class)?.guard ?: 0
        val guardModifier = if (guard > 0) 0.25 else 1.0

        val modifiedDamage = applicableResistances.fold(incomingDamage.toDouble()) { acc, resistance ->
            acc * resistance.rollModifier
        } * guardModifier

        return modifiedDamage.toInt().coerceAtLeast(1)
    }
}