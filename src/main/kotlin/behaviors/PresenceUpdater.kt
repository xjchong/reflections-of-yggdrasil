package behaviors

import attributes.Presence
import attributes.Senses
import entity.AnyEntity
import entity.getAttribute
import entity.isAlliedWith
import entity.position
import extensions.optional
import game.GameContext
import utilities.DijkstraMapping


object PresenceUpdater : ForegroundBehavior(Presence::class) {

    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        if (entity.position == entity.getAttribute(Presence::class)?.lastPosition) return true
        val senses = entity.getAttribute(Senses::class) ?: return true
        val sensedEnemies = senses.sensedEntities.filter { !entity.isAlliedWith(it) }
        val presence = entity.getAttribute(Presence::class) ?: return false

        presence.avoidanceMap.clear()
        presence.avoidanceMap.putAll(DijkstraMapping
                .getAvoidanceMap(sensedEnemies.map { it.position }.toSet(), 10) {
                    context.world.fetchBlockAt(it).optional?.isWall == true
                })

        return true
    }
}