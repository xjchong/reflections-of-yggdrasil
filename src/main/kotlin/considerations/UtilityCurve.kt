package considerations

import kotlin.math.pow


sealed class UtilityCurve {

    companion object {
        const val CLAMPED_MIN = 0.0
        const val CLAMPED_MAX = 1.0
    }

    fun evaluate(input: Double): Double {
        return unClampedEvaluation(input).coerceIn(CLAMPED_MIN, CLAMPED_MAX)
    }

    protected abstract fun unClampedEvaluation(input: Double): Double

    abstract fun print(input: Double?): String

}

data class ConstantCurve(private val output: Double) : UtilityCurve() {

    override fun unClampedEvaluation(input: Double): Double {
        return output
    }

    override fun print(input: Double?): String {
        return "[Constant: output($output)]" +
                if (input != null) {
                    "{input($input) -> ${unClampedEvaluation(input)}}"
                } else ""
    }
}

data class LinearCurve(private val slope: Double,
                       private val hShift: Double,
                       private val vShift: Double) : UtilityCurve() {

    override fun unClampedEvaluation(input: Double): Double {
        return (slope * (input - hShift)) + vShift
    }

    override fun print(input: Double?): String {
        return "[Linear: slope($slope), hShift($hShift), vShift($vShift)]" +
                if (input != null) {
                    "{input($input) -> ${unClampedEvaluation(input)}}"
                } else ""
    }
}

data class QuadraticCurve(private val slope: Double,
                          private val inflection: Double,
                          private val hShift: Double,
                          private val vShift: Double) : UtilityCurve() {

    override fun unClampedEvaluation(input: Double): Double {
        return (slope * (input - hShift).pow(inflection)) + vShift
    }

    override fun print(input: Double?): String {
        return "[Quadratic: slope($slope), inflection($inflection), hShift($hShift), vShift($vShift)]" +
                if (input != null) {
                    "{input($input) -> ${unClampedEvaluation(input)}}"
                } else ""
    }
}
