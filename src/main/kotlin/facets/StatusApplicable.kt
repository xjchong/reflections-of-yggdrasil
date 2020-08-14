package facets

import GameColor
import attributes.facet.AttackableDetails
import attributes.EntityTime
import attributes.facet.StatusApplicableDetails
import commands.ApplyStatus
import commands.Guard
import entity.getAttribute
import entity.position
import entity.spendTime
import events.Notice
import events.Special
import game.GameContext
import models.Heal
import models.Poison
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object StatusApplicable : BaseFacet<GameContext>(StatusApplicableDetails::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        var response: Response = Pass

        when (command) {
            is ApplyStatus -> {
                val effect = command.effect

                if (Math.random() <= effect.chance) {
                    response = when (effect.type) {
                        is Heal -> applyHeal(command)
                        is Poison -> applyPoison(command)
                    }
                }
            }

            is Guard -> {
                val (context, guarder) = command

                guarder.findAttribute(StatusApplicableDetails::class).ifPresent { statusDetails ->
                    statusDetails.guard = 1
                    guarder.spendTime(EntityTime.GUARD)
                    context.world.flash(guarder.position, GameColor.GUARD_FLASH)
                    context.world.observeSceneBy(guarder, "The $guarder guards against attacks!")
                    response = Consumed
                }
            }
        }

        return response
    }

    private fun applyHeal(command: ApplyStatus): Response {
        val (context, applicable, applier, effect) = command
        val combatStats = applicable.getAttribute(AttackableDetails::class) ?: return Pass

        combatStats.gainHealth(effect.potency)
        context.world.observeSceneBy(applicable, "The $applier heals the $applicable for ${effect.potency}!", Special)

        return Consumed
    }

    private fun applyPoison(command: ApplyStatus): Response {
        val (context, applicable, applier, effect) = command
        val details = applicable.getAttribute(StatusApplicableDetails::class) ?: return Pass
        val luck = applicable.getAttribute(AttackableDetails::class)?.luck ?: 0.0

        if (Math.random() < luck / 2) return Consumed

        details.poison = effect.potency
        context.world.observeSceneBy(applicable, "The $applicable is poisoned by the $applier!", Notice)

        return Consumed
    }
}