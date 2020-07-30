package commands

import entity.AnyEntity
import game.GameContext
import models.AttackType
import org.hexworks.amethyst.api.entity.EntityType


data class AttemptAttack(
        override val context: GameContext,
        override val source: AnyEntity,
        override val target: AnyEntity
) : EntityAction<EntityType, EntityType>

data class Attack(
        override val context: GameContext,
        override val source: AnyEntity,
        override val target: AnyEntity,
        val details: AttackDetails
) : EntityAction<EntityType, EntityType>

data class AttackDetails(val damage: Int, val description: String, val type: AttackType)