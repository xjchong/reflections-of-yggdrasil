package commands

import entity.AnyEntity
import game.GameContext
import models.StatusEffect
import org.hexworks.amethyst.api.entity.EntityType


data class ApplyStatus(
        override val context: GameContext,
        override val source: AnyEntity,
        override val target: AnyEntity,
        val effect: StatusEffect
) : EntityAction<EntityType, EntityType>