package considerations

import attributes.facet.AttackableDetails
import entity.getAttribute


class HealthConsideration(utilityCurve: UtilityCurve) : Consideration(utilityCurve) {

    override fun minInput(considerationContext: ConsiderationContext): Double {
        return 0.0
    }

    override fun maxInput(considerationContext: ConsiderationContext): Double {
        return considerationContext.source.getAttribute(AttackableDetails::class)?.maxHealth?.toDouble() ?: 0.0
    }

    override fun currentInput(considerationContext: ConsiderationContext): Double {
        return considerationContext.source.getAttribute(AttackableDetails::class)?.health?.toDouble() ?: 0.0
    }
}
