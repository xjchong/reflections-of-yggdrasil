package considerations

import entity.AnyEntity
import game.GameContext


data class ConsiderationContext(val context: GameContext,
                                val source: AnyEntity,
                                val extras: ConsiderationExtras = ConsiderationExtras())