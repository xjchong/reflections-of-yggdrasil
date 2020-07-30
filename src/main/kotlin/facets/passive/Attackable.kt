package facets.passive

import GameColor
import attributes.CombatStats
import attributes.FocusTarget
import attributes.KillTarget
import attributes.StatusDetails
import commands.ApplyStatus
import commands.Attack
import commands.Destroy
import entity.defenseModifier
import entity.executeBlockingCommand
import entity.getAttribute
import entity.isAlliedWith
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Attackable : BaseFacet<GameContext>(CombatStats::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Attack::class) { (context, attacker, target, details) ->
            if (attacker.isAlliedWith(target)) return@responseWhenCommandIs Pass

            val targetCombatStats = target.getAttribute(CombatStats::class) ?: return@responseWhenCommandIs Pass
            val finalDamage = (details.damage * target.defenseModifier).toInt().coerceAtLeast(1)

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
                context.world.flash(attacker, GameColor.ATTACK_FLASH)

                if (health <= 0) {
                    context.world.flash(target, GameColor.DESTROY_FLASH)
                    target.executeBlockingCommand(Destroy(context, target, cause = "the $attacker"))
                } else {
                    if (target.getAttribute(StatusDetails::class)?.guard ?: 0 > 0) {
                        context.world.flash(target, GameColor.GUARD_FLASH)
                    } else {
                        context.world.flash(target, GameColor.DAMAGE_FLASH)
                    }
                }

                for (effect in details.effects) {
                    target.executeBlockingCommand(ApplyStatus(context, attacker, target, effect))
                }
            }

            Consumed
        }
    }
}