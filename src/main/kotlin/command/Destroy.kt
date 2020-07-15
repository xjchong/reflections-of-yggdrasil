package command

import extension.AnyGameEntity
import extension.GameCommand
import org.hexworks.amethyst.api.entity.EntityType
import world.GameContext

data class Destroy(
    override val context: GameContext,
    override val source: AnyGameEntity,
    val target: AnyGameEntity,
    val cause: String = "natural causes."
) : GameCommand<EntityType>
