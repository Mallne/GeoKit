package cloud.mallne.geokit.coordinates.execution

import kotlin.math.PI
import kotlin.math.abs

internal object CalculationExtensions {

    /**
     * Default tolerance (epsilon) for approximate floating-point comparisons.
     * 1e-6 is a common value, but for high-precision geodetic work, a smaller
     * value like 1e-12 or 1e-15 is often preferred to check for convergence.
     */
    private const val EPSILON: Double = 1e-6

    /**
     * Converts the angle measured in radians to an approximately equivalent angle measured in degrees.
     */
    fun Double.toDegrees(): Double {
        val degrees = this % (2 * PI)
        return degrees * 180 / PI
    }

    /**
     * Converts the angle measured in degrees to an approximately equivalent angle measured in radians.
     */
    fun Double.toRadians(): Double {
        val radians = this % 360
        return radians * PI / 180
    }

    infix fun Double.`~`(other: Double): Boolean {
        return this approx other
    }

    infix fun Double.approx(other: Double): Boolean {
        return abs(this - other) < EPSILON
    }
}