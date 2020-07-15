package command

import extension.AnyGameEntity
import org.hexworks.amethyst.api.entity.EntityType
import world.GameContext

data class Attack(
    override val context: GameContext,
    override val source: AnyGameEntity,
    override val target: AnyGameEntity
) : EntityAction<EntityType, EntityType>