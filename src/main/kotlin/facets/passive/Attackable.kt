package facets.passive

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
import models.AttackDetails
import models.Resistance
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType


object Attackable : BaseFacet<GameContext>(CombatStats::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Attack::class) { (context, attacker, target, details) ->
            val targetCombatStats = target.getAttribute(CombatStats::class) ?: return@responseWhenIs Pass
            val finalDetails = target.applyResistances(details)
            val finalDamage = finalDetails.damage

            attacker.spendTime(details.timeCost)

            targetCombatStats.run {
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

                context.world.observeSceneBy(attacker, "The $attacker ${details.description} the $target for ${finalDamage}!")

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

                for (effect in details.effects) {
                    target.executeCommand(ApplyStatus(context, attacker, target, effect))
                }
            }

            Consumed
        }
    }

    private fun AnyEntity.applyResistances(details: AttackDetails): AttackDetails {
        val innateResistances: List<Resistance> = getAttribute(Resistances::class)?.resistances?.filter {
            it.type == details.type
        } ?: listOf()

        val equipmentResistances: List<Resistance> = getAttribute(Equipments::class)?.resistancesFor(details.type)
                ?: listOf()

        val guard = getAttribute(StatusDetails::class)?.guard ?: 0
        var incomingDamage = details.damage.toDouble()

        innateResistances.forEach { incomingDamage *= it.rollModifier }
        equipmentResistances.forEach { incomingDamage *= it.rollModifier }
        if (guard > 0) incomingDamage *= 0.25

        val finalDamage = incomingDamage.toInt().coerceAtLeast(1)

        return AttackDetails(finalDamage, details.description, details.type, details.effects, details.timeCost)
    }
}