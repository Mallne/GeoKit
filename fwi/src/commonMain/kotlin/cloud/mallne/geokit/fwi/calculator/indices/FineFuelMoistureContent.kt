package cloud.mallne.geokit.fwi.calculator.indices

import cloud.mallne.units.*
import cloud.mallne.units.Length.Companion.kilometers
import cloud.mallne.units.Length.Companion.millimeters
import cloud.mallne.units.Probability.Companion.percent
import cloud.mallne.units.Temperature.Companion.celsius
import cloud.mallne.units.Time.Companion.hours
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

object FineFuelMoistureContent {
    internal const val FFMC_DEFAULT = 85.0
    internal val FFMC_INTERCEPT = 0.5 * millimeters
    private const val C_FFMC = 14875.0 / 101.0 // 147.277...

    /**
     * Convert to fine fuel moisture content (%)
     * @param ffmc [Double]       Fine Fuel Moisture Code (FFMC)
     * @return [Double]           fine fuel moisture content (%)
     */
    fun ffmcToMcffmc(ffmc: Double): Measure<Probability> = (C_FFMC * (101.0 - ffmc) / (59.5 + ffmc)) * percent

    /**
     * Convert to FFMC
     * @param mcffmc [Double]     fine fuel moisture content (%)
     * @return [Double]           FFMC
     */
    fun mcffmcToFfmc(mcffmc: Measure<Probability>): Double =
        59.5 * (250.0 - (mcffmc `in` percent)) / (C_FFMC + (mcffmc `in` percent))

    /**
     * Calculate hourly fine fuel moisture content. Needs to be converted to get FFMC
     *
     * @param lastMc        [Double]  Previous fine fuel moisture content (%)
     * @param temp          [Double]  Temperature (Celcius)
     * @param rh            [Double]  Relative Humidity (percent, 0-100)
     * @param ws            [Double]  Wind Speed (km/h)
     * @param rain          [Double]  Rainfall AFTER intercept (mm)
     * @param timeIncrement [Double]  Duration of timestep (hr, default 1.0)
     * @return              [Double]  Hourly fine fuel moisture content (%)
     */
    operator fun invoke(
        lastMc: Measure<Probability>,
        temp: Measure<Temperature>,
        rh: Measure<Probability>,
        ws: Measure<Velocity>,
        rain: Measure<Length>,
        timeIncrement: Double = 1.0
    ): Measure<Probability> {
        val rf = 42.5
        val drf = 0.0579
        // use moisture directly instead of converting to/from ffmc
        // expects any rain intercept to already be applied
        var mo = lastMc `in` percent

        if ((rain `in` millimeters) != 0.0) {
            // duplicated in both formulas, so calculate once
            // lastmc == mo, but use lastmc since mo changes after first equation
            mo += rf * (rain `in` millimeters) * exp(-100.0 / (251.0 - (lastMc `in` percent))) * (1.0 - exp(-6.93 / (rain `in` millimeters)))
            if ((lastMc `in` percent) > 150.0) {
                mo += 0.0015 * ((lastMc `in` percent) - 150.0).pow(2) * sqrt((rain `in` millimeters))
            }
            if (mo > 250.0) {
                mo = 250.0
            }
        }
        // duplicated in both formulas, so calculate once
        val e1 = 0.18 * (21.1 - (temp `in` celsius)) * (1.0 - exp(-0.115 * (rh `in` percent)))
        val ed = 0.942 * (rh `in` percent).pow(0.679) + (11.0 * exp(((rh `in` percent) - 100.0) / 10.0)) + e1
        val ew = 0.618 * (rh `in` percent).pow(0.753) + (10.0 * exp(((rh `in` percent) - 100.0) / 10.0)) + e1

        val m = if (mo < ed) ew else ed
        if (mo != ed) {
            // these are the same formulas with a different value for a1
            val a1 = if (mo > ed) (rh `in` percent) / 100.0 else (100.0 - (rh `in` percent)) / 100.0
            val k0OrK1 =
                0.424 * (1.0 - a1.pow(1.7)) + (0.0694 * sqrt((ws `in` (kilometers / hours))) * (1.0 - a1.pow(8.0)))
            val kdOrKw = 2.0 * drf * k0OrK1 * exp(0.0365 * (temp `in` celsius))
            return (m + (mo - m) * 10.0.pow(-kdOrKw * timeIncrement)) * percent
        }
        return m * percent
    }
}