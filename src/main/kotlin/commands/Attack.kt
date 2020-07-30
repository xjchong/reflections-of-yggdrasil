package commands

import entity.AnyEntity
import game.GameContext
import models.AttackDetails
import org.hexworks.amethyst.api.entity.EntityType


data class Attack(
        override val context: GameContext,
        override val source: AnyEntity,
        override val target: AnyEntity,
        val details: AttackDetails
) : EntityAction<EntityType, EntityType>

