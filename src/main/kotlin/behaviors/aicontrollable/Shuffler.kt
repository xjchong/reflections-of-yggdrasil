package behaviors.aicontrollable

import attributes.behavior.*
import commands.Move
import considerations.Consideration
import considerations.ConsiderationContext
import entity.GameEntity
import entity.canPass
import entity.getAttribute
import entity.position
import game.GameContext
import models.Plan
import org.hexworks.zircon.api.data.Position3D

object Shuffler : AiControllableBehavior() {

    override suspend fun getPlans(
        context: GameContext,
        entity: GameEntity,
        considerations: List<Consideration>
    ): List<Plan> {
        val shuffleBias = entity.getAttribute(ShufflerDetails::class) ?: return listOf()
        val currentBiasType = shuffleBias.type
        val fallbackBiasType = currentBiasType.randomlyRotated()
        val considerationContext = ConsiderationContext(context, entity)
        val command = Move(context, entity) {
            var nextPosition =
                getNextPositionForBias(
                    currentBiasType,
                    entity.position
                )

            for (nextBias in listOf(currentBiasType.flipped(), fallbackBiasType, fallbackBiasType.flipped())) {
                if (entity.canPass(context, nextPosition)) break

                shuffleBias.type = nextBias
                nextPosition = getNextPositionForBias(
                    nextBias,
                    entity.position
                )
            }

            nextPosition
        }

        val plans = mutableListOf<Plan>()

        plans.addPlan(command, considerations, considerationContext)

        return plans
    }

    private fun getNextPositionForBias(shuffleBiasType: ShuffleBiasType, position: Position3D): Position3D {
        return when(shuffleBiasType) {
            EastShuffle -> position.withRelativeX(1)
            SouthShuffle -> position.withRelativeY(1)
            WestShuffle -> position.withRelativeX(-1)
            NorthShuffle -> position.withRelativeY(-1)
        }
    }

    private fun ShuffleBiasType.flipped(): ShuffleBiasType {
        return when (this) {
            EastShuffle -> WestShuffle
            SouthShuffle -> NorthShuffle
            WestShuffle -> EastShuffle
            NorthShuffle -> SouthShuffle
        }
    }

    private fun ShuffleBiasType.randomlyRotated(): ShuffleBiasType {
        val coinToss = Math.random() < 0.5
        return when (this) {
            EastShuffle -> if (coinToss) SouthShuffle else NorthShuffle
            SouthShuffle -> if (coinToss) WestShuffle else EastShuffle
            WestShuffle -> if (coinToss) NorthShuffle else SouthShuffle
            NorthShuffle -> if (coinToss) EastShuffle else WestShuffle
        }
    }
}