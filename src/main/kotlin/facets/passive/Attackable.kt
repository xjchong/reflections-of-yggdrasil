package facets.passive

import GameColor
import attributes.CombatStats
import attributes.FocusTarget
import commands.Attack
import commands.Destroy
import entity.*
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Maybe

object Attackable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(Attack::class) { (context, attacker, target) ->
            if (!attacker.isPlayer && !target.isPlayer) return@responseWhenCommandIs Pass

            target.getAttribute(CombatStats::class)?.run {
                var incomingDamage = attacker.attackRating

                attacker.getAttribute(CombatStats::class)?.dockStamina(20)
                incomingDamage *= target.defenseModifier
                dockHealth(incomingDamage.toInt())

                // Update focus targets of the combatants.
                if (health > 0) {
                    attacker.getAttribute(FocusTarget::class)?.targetProperty?.updateValue(Maybe.of(target))
                }

                target.getAttribute(FocusTarget::class)?.run {
                    if (!targetProperty.value.isPresent) {
                        targetProperty.updateValue(Maybe.of(attacker))
                    }
                }

                context.world.observeSceneBy(attacker, "The $attacker hits the $target for ${incomingDamage.toInt()}!")
                context.world.flash(attacker, GameColor.ATTACK_FLASH)

                if (health <= 0) {
                    context.world.flash(target, GameColor.DESTROY_FLASH)
                    target.executeBlockingCommand(Destroy(context, target, cause = "the $attacker"))
                } else {
                    context.world.flash(target, GameColor.DAMAGE_FLASH)
                }
            }

            Consumed
        }
    }
}