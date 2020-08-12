package considerations

import entity.AnyEntity
import models.AttackStrategy


data class ConsiderationExtras(
        val attackStrategy: AttackStrategy? = null,
        val target: AnyEntity? = null
)