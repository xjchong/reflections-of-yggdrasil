package facets.passive

import GameColor
import attributes.CombatStats
import attributes.StatusDetails
import commands.Guard
import commands.Heal
import entity.getAttribute
import events.Critical
import events.Special
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object StatusInflictable : BaseFacet<GameContext>(StatusDetails::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        var response: Response = Pass

        command.whenCommandIs(Guard::class) { (context, entity) ->
            if (entity.getAttribute(CombatStats::class)?.dockStamina(5) == false) {
                context.world.observeSceneBy(entity, "The $entity doesn't have enough stamina to guard!", Critical)
                return@whenCommandIs false
            }

            entity.findAttribute(StatusDetails::class).ifPresent { statusDetails ->
                statusDetails.guard = 1
                context.world.flash(entity, GameColor.GUARD_FLASH)
                context.world.observeSceneBy(entity, "The $entity guards against attacks!")
                response = Consumed
            }

            true
        }

        command.whenCommandIs(Heal::class) { (context, source, target, amount) ->
            target.findAttribute(CombatStats::class).ifPresent { combatStats ->
                combatStats.gainHealth(amount)
                context.world.observeSceneBy(target, "The $source heals the $target for $amount!", Special)
                response = Consumed
            }

            true
        }

        return response
    }
}