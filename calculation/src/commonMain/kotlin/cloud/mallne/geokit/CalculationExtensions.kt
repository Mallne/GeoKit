package cloud.mallne.geokit

import kotlin.math.pow
import kotlin.math.sqrt

internal object CalculationExtensions {
    const val PI = kotlin.math.PI

    // Dot product for direction vectors
    infix fun DoubleArray.dot(other: DoubleArray) =
        foldIndexed(0.0) { i, acc, cur -> acc + cur * other[i] }

    // Magnitude squared for a direction vector
    fun DoubleArray.magnitudeSquared() =
        fold(0.0) { acc, cur -> acc + cur.pow(2) }

    fun DoubleArray.magnitude() = sqrt(magnitudeSquared())

    fun DoubleArray.normalize(): DoubleArray {
        val magnitude = magnitude()
        return if (magnitude > 0) {
            this.map {
                it / magnitude
            }.toDoubleArray()
        } else {
            this
        }
    }

    // Scalar multiplication for a direction vector
    operator fun Double.times(vector: DoubleArray) = vector.map { this * it }.toDoubleArray()
    operator fun DoubleArray.times(double: Double) = double * this

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
}