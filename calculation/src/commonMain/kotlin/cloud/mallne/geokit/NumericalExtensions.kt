package cloud.mallne.geokit

import kotlin.math.pow
import kotlin.math.round


internal object NumericalExtensions {
    /**
     * Rounds a Double to the specified number of decimal places.
     *
     * @param decimals The number of decimal places to round to. Must be non-negative.
     * @return The rounded Double.
     * @throws IllegalArgumentException if the number of decimals is negative.
     */
    internal fun Double.round(decimals: Int): Double {
        require(decimals >= 0) { "Decimals must be non-negative" }
        if (decimals == 0) {
            return round(this)
        }
        val multiplier = 10.0.pow(decimals)
        return round(this * multiplier) / multiplier
    }
}