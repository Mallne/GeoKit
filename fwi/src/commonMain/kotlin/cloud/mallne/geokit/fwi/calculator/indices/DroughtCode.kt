package cloud.mallne.geokit.fwi.calculator.indices

import cloud.mallne.units.*
import cloud.mallne.units.Length.Companion.millimeters
import cloud.mallne.units.Probability.Companion.percent
import cloud.mallne.units.Temperature.Companion.celsius
import kotlin.math.exp
import kotlin.math.ln

object DroughtCode {
    internal const val DC_DEFAULT = 15.0
    private val DC_INTERCEPT = 2.8 * millimeters
    private const val DC_REGRESSION = 1.5e-2
    private const val DC_OFFSET_TEMP = 0.0

    /**
     * Convert to DC moisture content (%)
     * @param dc [Double]        Drought Code (DC)
     * @return   [Double]        DC moisture content (%)
     */
    internal fun dcToMcdc(dc: Double): Measure<Probability> = (400.0 * exp(-dc / 400.0)) * percent

    /**
     * Convert to DC
     * @param mcdc  [Double]     DC moisture content (%)
     * @return      [Double]     DC
     */
    internal fun mcdcToDc(mcdc: Measure<Probability>): Double = 400.0 * ln(400.0 / (mcdc `in` percent))

    /**
     * Calculate drought code moisture content
     *
     * @param lastMcdc           [Double]  Previous drought code moisture content (%)
     * @param hour               [Double]    Time of day (hr)
     * @param temp               [Double]  Temperature (Celsius)
     * @param prec               [Double]  Hourly precipitation (mm)
     * @param sunrise            [Double]  Sunrise (hr)
     * @param sunset             [Double]  Sunset (hr)
     * @param precCumulativePrev [Double]  Cumulative precipitation since start of rain (mm)
     * @param timeIncrement      [Double]  Duration of timestep (hr, default 1.0)
     * @return                   [Double]  Hourly drought code moisture content (%)
     */
    operator fun invoke(
        lastMcdc: Measure<Probability>,
        hour: Double,
        temp: Measure<Temperature>,
        prec: Measure<Length>,
        sunrise: Double,
        sunset: Double,
        precCumulativePrev: Measure<Length>,
        timeIncrement: Double = 1.0
    ): Measure<Probability> {
        val lastMcdcU = lastMcdc `in` percent
        var mr: Double

        // 1. Wetting Phase
        if (precCumulativePrev + prec > DC_INTERCEPT) {
            val rw = if (precCumulativePrev <= DC_INTERCEPT) {
                // Just passed the interception threshold
                ((precCumulativePrev `in` millimeters) + (prec `in` millimeters)) * 0.83 - (1.27)
            } else {
                // Already above the threshold
                (prec `in` millimeters) * 0.83
            }
            mr = lastMcdcU + (3.937 * rw) / 2.0
        } else {
            mr = lastMcdcU
        }

        if (mr > 400.0) mr = 400.0

        // 2. Drying Phase
        // Logic handles the case where sunset might be represented as > 24 hours
        val isDaytime = (hour in sunrise..sunset) ||
                (hour < 6.0 && (hour + 24.0) in sunrise..sunset)

        val mcdc = if (isDaytime) {
            val pe = if (temp > (0.0 * celsius)) {
                DC_REGRESSION * ((temp `in` celsius) + DC_OFFSET_TEMP) + (3.0 / 16.0)
            } else {
                0.0
            }

            val invtau = pe / 400.0
            mr * exp(-timeIncrement * invtau)
        } else {
            mr
        }

        return mcdc.coerceAtMost(400.0) * percent
    }
}