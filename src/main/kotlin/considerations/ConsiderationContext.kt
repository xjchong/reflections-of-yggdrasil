package considerations

import entity.GameEntity
import game.GameContext


data class ConsiderationContext(val context: GameContext,
                                val source: GameEntity,
                                val extras: ConsiderationExtras = ConsiderationExtras())