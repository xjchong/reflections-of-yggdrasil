package considerations


abstract class Consideration(private val utilityCurve: UtilityCurve) {

    protected abstract fun minInput(considerationContext: ConsiderationContext): Double
    protected abstract fun maxInput(considerationContext: ConsiderationContext): Double
    protected abstract fun currentInput(considerationContext: ConsiderationContext): Double

    fun evaluate(considerationContext: ConsiderationContext): Double {
        return utilityCurve.evaluate(normalizedInput(considerationContext))
    }

    private fun normalizedInput(considerationContext: ConsiderationContext): Double {
        val minInput = minInput(considerationContext)
        val maxInput = maxInput(considerationContext)

        if (minInput > maxInput) throw IllegalStateException(
                "${javaClass.simpleName} cannot normalize ${considerationContext.toString()} " +
                        "because minInput($minInput) > maxInput($maxInput). " +
                        "Ensure that for any context, minInput <= maxInput."
        )

        val currentInput = currentInput(considerationContext).coerceIn(minInput, maxInput)

        return if (minInput == maxInput) {
            return currentInput
        } else {
            (currentInput - minInput) / (maxInput - minInput)
        }
    }
}

