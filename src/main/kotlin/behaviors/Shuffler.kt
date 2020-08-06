package behaviors

import attributes.*
import commands.Move
import entity.AnyEntity
import entity.executeBlockingCommand
import entity.getAttribute
import entity.position
import extensions.optional
import game.GameContext
import org.hexworks.zircon.api.data.Position3D

object Shuffler : ForegroundBehavior() {

    val GOAL_KEY = "Shuffle"

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val shuffleBias = entity.getAttribute(ShuffleBias::class) ?: return false
        val position = entity.position

        if (position.isUnknown) return false

        val currentBiasType = shuffleBias.type
        val fallbackBiasType = currentBiasType.randomlyRotated()
        var nextPosition = getNextPositionForBias(currentBiasType, position)

        for (nextBias in listOf(currentBiasType.flipped(), fallbackBiasType, fallbackBiasType.flipped())) {
            if (context.world.fetchBlockAt(nextPosition).optional?.isObstructed == true) {
                shuffleBias.type = nextBias
                nextPosition = getNextPositionForBias(nextBias, position)
            }
        }

        return entity.addShuffleGoal(context, nextPosition)
    }

    private fun AnyEntity.addShuffleGoal(context: GameContext, nextPosition: Position3D): Boolean {
        return getAttribute(Goals::class)?.list?.add(Goal(GOAL_KEY, 20) {
            executeBlockingCommand(Move(context, this, nextPosition))
        }) == true
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