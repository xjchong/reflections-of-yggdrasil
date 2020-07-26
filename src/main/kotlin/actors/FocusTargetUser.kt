package actors

import attributes.FocusTarget
import entity.getAttribute
import entity.position
import entity.sensedPositions
import extensions.optional
import game.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseActor
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType

object FocusTargetUser : BaseActor<GameContext>(FocusTarget::class) {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (context.inBackground) return true

        val focusTarget = entity.getAttribute(FocusTarget::class) ?: return true
        val targetPosition = focusTarget.target.optional?.position ?: return true

        if (!entity.sensedPositions.contains(targetPosition)) {
            focusTarget.clearTarget()
        }

        return true
    }

    override suspend fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        TODO("Not yet implemented")
    }
}