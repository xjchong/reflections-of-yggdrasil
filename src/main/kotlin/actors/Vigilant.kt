package actors

import attributes.KillTarget
import attributes.Vigilance
import entity.getAttribute
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseActor
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType


object Vigilant : BaseActor<GameContext>(Vigilance::class) {

    /** If this entity can see another entity, and that other entity is targeting this one, then
     * this entity will become alert. Otherwise, this entity slowly relaxes its alert level.
     */
    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val world = context.world
        val vigilance = entity.getAttribute(Vigilance::class) ?: return true

        for (visiblePos in world.findVisiblePositionsFor(entity)) {
            for (potentialKiller in world.fetchEntitiesAt(visiblePos)) {
                if (potentialKiller.getAttribute(KillTarget::class)?.target == entity) {
                    println("$potentialKiller wants to kill $entity")
                    vigilance.alert()
                    return true
                }
            }
        }

        vigilance.relax()
        return true
    }

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return Pass
    }
}