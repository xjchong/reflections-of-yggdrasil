package considerations


class ConstantConsideration(output: Double) : Consideration(ConstantCurve(output)) {

    override fun minInput(considerationContext: ConsiderationContext): Double {
        return 1.0
    }

    override fun maxInput(considerationContext: ConsiderationContext): Double {
        return 1.0
    }

    override fun currentInput(considerationContext: ConsiderationContext): Double {
        return 1.0
    }
}