package behaviors

import attributes.Goal
import attributes.Goals
import attributes.Senses
import commands.Move
import entity.*
import game.GameContext
import utilities.AStar

object Chaser : ForegroundBehavior(Goals::class) {

    val GOAL_KEY = "Chase"

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        val senses = entity.getAttribute(Senses::class) ?: return false

        for (sensedEntity in senses.sensedEntities) {
            if (!entity.isAlliedWith(sensedEntity)) {
                return entity.addChaseGoal(context, sensedEntity)
            }
        }

        return false
    }

    private fun AnyEntity.addChaseGoal(context: GameContext, target: AnyEntity): Boolean {
        return getAttribute(Goals::class)?.list?.add(Goal(GOAL_KEY, 60) {
            val nextPosition = AStar.getPath(position, target.position) { from, to ->
                context.world.getMovementCost(this, from, to)
            }.first()

            executeBlockingCommand(Move(context, this, nextPosition))
        }) == true
    }
}