package actors

import attributes.EnergyLevel
import commands.Destroy
import commands.ExpendEnergy
import entity.*
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseActor
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType


object EnergyExpender : BaseActor<GameContext>(EnergyLevel::class) {

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(ExpendEnergy::class) { (context, entity, energy) ->
            with (entity) {
                energyLevel.currentEnergy -= energy

                whenStarved {
                    executeBlockingCommand(Destroy(context, entity, cause = "starvation"))
                }
            }

            Consumed
        }
    }

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.inBackground) return true

        entity.whenTypeIs<EnergyUserType> {
            executeCommand(ExpendEnergy(context, this, energy = 2))
        }

        return true
    }
}