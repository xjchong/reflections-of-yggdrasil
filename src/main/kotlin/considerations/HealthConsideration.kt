package considerations

import attributes.CombatStats
import entity.getAttribute


class HealthConsideration(utilityCurve: UtilityCurve) : Consideration(utilityCurve) {

    override fun minInput(considerationContext: ConsiderationContext): Double {
        return 0.0
    }

    override fun maxInput(considerationContext: ConsiderationContext): Double {
        return considerationContext.source.getAttribute(CombatStats::class)?.maxHealth?.toDouble() ?: 0.0
    }

    override fun currentInput(considerationContext: ConsiderationContext): Double {
        return considerationContext.source.getAttribute(CombatStats::class)?.health?.toDouble() ?: 0.0
    }
}
