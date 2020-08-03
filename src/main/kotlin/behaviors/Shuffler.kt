package behaviors

import attributes.*
import commands.AttemptAnyAction
import commands.Move
import entity.AnyEntity
import entity.getAttribute
import entity.position
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.Pass
import org.hexworks.zircon.api.data.Position3D

object Shuffler : ForegroundBehavior() {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val shuffleBias = entity.getAttribute(ShuffleBias::class) ?: return false
        val position = entity.position

        if (!position.isUnknown) {
            val currentBiasType = shuffleBias.type
            val fallbackBiasType = currentBiasType.randomlyRotated()
            var nextPosition = getNextPositionForBias(currentBiasType, position)

            for (nextBias in listOf(currentBiasType.flipped(), fallbackBiasType, fallbackBiasType.flipped())) {
                if (context.world.fetchBlockAt(nextPosition).optional?.isObstructed == true) {
                    shuffleBias.type = nextBias
                    nextPosition = getNextPositionForBias(nextBias, position)
                }
            }

            nextPosition.let {
                if (entity.executeCommand(AttemptAnyAction(context, entity, it)) == Pass) {
                    entity.executeCommand(Move(context, entity, it))
                }

                return true
            }
        }

        return false
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