package cloud.mallne.geokit.fwi.calculator

import kotlin.math.exp
import kotlin.math.ln

object DuffMoistureCode {
    internal const val DMC_DEFAULT = 6.0
    private const val DMC_INTERCEPT = 1.5
    private const val DMC_REGRESSION = 2.22e-4
    private const val DMC_OFFSET_TEMP = 0.0

    /**
     * Convert to duff moisture content (%)
     * @param dmc [Double]        Duff Moisture Code (DMC)
     * @return [Double]           duff moisture content (%)
     */
    fun dmcToMcdmc(dmc: Double): Double = (280.0 / exp(dmc / 43.43)) + 20.0

    /**
     * Convert to DMC
     * @param mcdmc      duff moisture content (%)
     * @return           DMC
     */
    fun mcdmcToDmc(mcdmc: Double): Double = 43.43 * ln(280.0 / (mcdmc - 20.0))

    /**
     * Calculate duff moisture content
     *
     * @param lastMcdmc          [Double]   Previous duff moisture content (%)
     * @param hour               [Double]    Time of day (hr)
     * @param temp               [Double]    Temperature (Celcius)
     * @param rh                 [Double]    Relative Humidity (%)
     * @param prec               [Double]    Hourly precipitation (mm)
     * @param sunrise            [Double]    Sunrise (hr)
     * @param sunset             [Double]    Sunset (hr)
     * @param precCumulativePrev [Double]    Cumulative precipitation since start of rain (mm)
     * @param timeIncrement      [Double]    Duration of timestep (hr, default 1.0)
     * @return                   [Double]    Hourly duff moisture content (%)
     */
    operator fun invoke(
        lastMcdmc: Double,
        hour: Double,
        temp: Double,
        rh: Double,
        prec: Double,
        sunrise: Double,
        sunset: Double,
        precCumulativePrev: Double,
        timeIncrement: Double = 1.0 // duration of timestep, in hours
    ): Double {
        var currentTemp = temp
        var mr: Double

        // 1. Wetting Phase
        if (precCumulativePrev + prec > DMC_INTERCEPT) {
            val rw = if (precCumulativePrev <= DMC_INTERCEPT) {
                (precCumulativePrev + prec) * 0.92 - 1.27
            } else {
                prec * 0.92
            }

            val lastDmc = mcdmcToDmc(lastMcdmc)
            val b = when {
                lastDmc <= 33.0 -> 100.0 / (0.3 * lastDmc + 0.5)
                lastDmc <= 65.0 -> -1.3 * ln(lastDmc) + 14.0
                else -> 6.2 * ln(lastDmc) - 17.2
            }

            mr = lastMcdmc + (1.0e3 * rw) / (b * rw + 48.77)
        } else {
            mr = lastMcdmc
        }

        if (mr > 300.0) mr = 300.0

        // 2. Drying Phase
        // since sunset can be > 24, check hr + 24 (ignoring change between days)
        val isDaytime = (hour in sunrise..sunset) ||
                (hour < 6.0 && (hour + 24.0) in sunrise..sunset)

        val mcdmc = if (isDaytime) {
            if (currentTemp < 0.0) currentTemp = 0.0

            val rk = DMC_REGRESSION * (currentTemp + DMC_OFFSET_TEMP) * (100.0 - rh)
            val invtau = rk / 43.43
            (mr - 20.0) * exp(-timeIncrement * invtau) + 20.0
        } else {
            mr
        }

        return mcdmc.coerceAtMost(300.0)
    }
}