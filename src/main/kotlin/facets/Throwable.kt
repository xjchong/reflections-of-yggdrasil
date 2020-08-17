package facets

import attributes.AttackStrategies
import attributes.EntityTime
import attributes.facet.AttackableDetails
import attributes.flag.IsObstacle
import commands.Attack
import commands.Throw
import entity.*
import extensions.optional
import extensions.responseWhenIs
import game.GameContext
import models.Bash
import models.ThrowAttack
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Throwable : BaseFacet<GameContext>() {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenIs(Throw::class) { (context, throwable, thrower, path) ->
            val throwerAttackableDetails = thrower.getAttribute(AttackableDetails::class) ?: return@responseWhenIs Pass
            val throwerPower  = throwerAttackableDetails.power
            val throwerTechnique = throwerAttackableDetails.tech
            val attackStrategy = throwable.getAttribute(AttackStrategies::class)?.strategies?.firstOrNull()
            val attackType = attackStrategy?.type ?: Bash
            val throwableStrategy = ThrowAttack(throwable, attackType)
            val world = context.world

            // TODO: Fix how this spends double time on attack (time to throw, time to attack)
            thrower.spendTime(EntityTime.DEFAULT)

            for (position in path) {
                val currentBlock = world.fetchBlockAt(position).optional ?: continue
                val nextBlock = context.world.fetchBlockAt(position).optional ?: continue
                val entities = nextBlock.entities
                world.flash(position, throwable.tile.foregroundColor)

                for (entity in entities) {
                    if (!entity.hasAttribute<IsObstacle>()) continue
                    if (entity.executeCommand(Attack(context, entity, thrower, throwableStrategy)) == Pass) continue

                    world.removeEntity(throwable)
                    return@responseWhenIs Consumed
                }

                nextBlock.transfer(throwable, currentBlock, world)
            }

            Consumed
        }
    }

    fun getMaxRange(thrower: GameEntity, throwable: GameEntity): Int {
        return 8
    }

}