package facets.passive

import GameColor
import attributes.CombatStats
import attributes.StatusDetails
import commands.ApplyStatus
import commands.Guard
import entity.AnyEntity
import entity.getAttribute
import entity.position
import events.Critical
import events.Notice
import events.Special
import game.GameContext
import models.Heal
import models.Poison
import models.StatusEffect
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object StatusApplicable : BaseFacet<GameContext>(StatusDetails::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        var response: Response = Pass

        command.whenCommandIs(ApplyStatus::class) { (context, source, target, effect) ->
            if (Math.random() > effect.chance) return@whenCommandIs false

            response = when (effect.type) {
                is Heal -> applyHeal(context, source, target, effect)
                is Poison -> applyPoison(context, source, target, effect)
            }

            true
        }

        command.whenCommandIs(Guard::class) { (context, entity) ->
            if (entity.getAttribute(CombatStats::class)?.dockStamina(0) == false) {
                context.world.observeSceneBy(entity, "The $entity doesn't have enough stamina to guard!", Critical)
                return@whenCommandIs false
            }

            entity.findAttribute(StatusDetails::class).ifPresent { statusDetails ->
                statusDetails.guard = 1
                context.world.flash(entity.position, GameColor.GUARD_FLASH)
                context.world.observeSceneBy(entity, "The $entity guards against attacks!")
                response = Consumed
            }

            true
        }

        return response
    }

    private fun applyHeal(context: GameContext, source: AnyEntity, target: AnyEntity, effect: StatusEffect): Response {
        val targetCombatStats = target.getAttribute(CombatStats::class) ?: return Pass

        targetCombatStats.gainHealth(effect.potency)
        context.world.observeSceneBy(target, "The $source heals the $target for ${effect.potency}!", Special)

        return Consumed
    }

    private fun applyPoison(context: GameContext, source: AnyEntity, target: AnyEntity, effect: StatusEffect): Response {
        val targetStatusDetails = target.getAttribute(StatusDetails::class) ?: return Pass
        val luck = target.getAttribute(CombatStats::class)?.luck ?: 0.0
        if (Math.random() < luck / 2) {
            return Consumed
        }

        targetStatusDetails.poison = effect.potency
        context.world.observeSceneBy(target, "The $target is poisoned by the $source!", Notice)

        return Consumed
    }
}