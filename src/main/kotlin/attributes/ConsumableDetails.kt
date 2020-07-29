package attributes

import entity.AnyEntity
import game.GameContext
import org.hexworks.amethyst.api.Attribute


data class ConsumableDetails(val execute: (context: GameContext, source: AnyEntity, target: AnyEntity) -> Unit): Attribute
