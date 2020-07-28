package behaviors

import attributes.KillTarget
import attributes.Vigilance
import entity.AnyEntity
import entity.getAttribute
import entity.sensedPositions
import game.GameContext


object VigilanceUser : ForegroundBehavior(Vigilance::class) {

    /** If this entity can see another entity, and that other entity is targeting this one, then
     * this entity will become alert. Otherwise, this entity slowly relaxes its alert level.
     */
    override suspend fun foregroundUpdate(entity: AnyEntity, context: GameContext): Boolean {
        if (context.inBackground) return true
        val world = context.world
        val vigilance = entity.getAttribute(Vigilance::class) ?: return true

        for (sensedPos in entity.sensedPositions) {
            for (potentialKiller in world.fetchEntitiesAt(sensedPos)) {
                if (potentialKiller.getAttribute(KillTarget::class)?.target == entity) {
                    vigilance.alert()
                    return true
                }
            }
        }

        vigilance.relax()
        return true
    }
}