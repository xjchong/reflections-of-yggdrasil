package behaviors

import attributes.CombatStats
import attributes.Goal
import attributes.Goals
import attributes.Senses
import commands.Move
import entity.*
import extensions.neighbors
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.Pass
import org.hexworks.zircon.api.data.Position3D
import utilities.DijkstraMapping

object Fleer : ForegroundBehavior(Goals::class) {

    val GOAL_KEY = "Flee"

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        val world = context.world
        val senses = entity.getAttribute(Senses::class) ?: return false
        val sensedEnemies = senses.sensedEntities.filter { entity.isEnemiesWith(it) }

        return if (sensedEnemies.isNotEmpty()) entity.addFleeGoal(context, sensedEnemies) else false
    }

    private fun AnyEntity.addFleeGoal(context: GameContext, sensedEnemies: List<AnyEntity>): Boolean {
        val combatStats = getAttribute(CombatStats::class) ?: return false
        val senses = getAttribute(Senses::class) ?: return false

        return getAttribute(Goals::class)?.list?.add(Goal(
                GOAL_KEY,
                100 - (combatStats.health.toDouble() / combatStats.maxHealth.toDouble() * 100).toInt()) {
            val avoidanceMap = DijkstraMapping.getAvoidanceMap(sensedEnemies.map { it.position }.toSet(), senses.maxRange) {
                val block = context.world.fetchBlockAt(it).optional ?: return@getAvoidanceMap true
                !canPass(block)
            }

            var nextPosition = Position3D.unknown()
            var lowestAvoidanceVal: Int? = null

            for (neighbor in position.neighbors()) {
                val avoidanceVal = avoidanceMap[neighbor] ?: continue

                if (lowestAvoidanceVal == null || avoidanceVal < lowestAvoidanceVal) {
                    nextPosition = neighbor
                    lowestAvoidanceVal = avoidanceVal
                }
            }

            if (nextPosition == Position3D.unknown()) {
                Pass
            } else {
                executeBlockingCommand(Move(context, this, nextPosition))
            }
        }) == true
    }
}