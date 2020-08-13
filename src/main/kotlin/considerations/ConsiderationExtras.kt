package considerations

import entity.GameEntity
import models.AttackStrategy


data class ConsiderationExtras(
        val attackStrategy: AttackStrategy? = null,
        val target: GameEntity? = null
)