package cloud.mallne.geokit.fwi.calculator.indices

import cloud.mallne.geokit.fwi.calculator.Util.curingFactor
import cloud.mallne.units.*
import cloud.mallne.units.Length.Companion.kilometers
import cloud.mallne.units.Length.Companion.meters
import cloud.mallne.units.Probability.Companion.percent
import cloud.mallne.units.Time.Companion.hours
import cloud.mallne.units.Time.Companion.minutes
import kotlin.math.exp
import kotlin.math.pow

object GrasslandSpreadIndex {
    /**
     * Calculate Matted Grass Rate of Spread (ROS) based on Cheney (1998).
     *
     * @param ws  10m open wind speed (km/h)
     * @param mc  Moisture content in cured grass (%)
     * @param cur Percentage of grassland cured (%)
     * @return    Rate of Spread in m/min
     */
    private fun mattedGrassSpreadRos(
        ws: Measure<Velocity>,
        mc: Measure<Probability>,
        cur: Measure<Probability>
    ): Measure<UnitsRatio<Length, Time>> {
        // Wind function (fw)
        val wsu = ws `in` (kilometers / hours)
        val mcu = mc `in` percent
        val fw = 16.67 * if (wsu < 5.0) {
            0.054 + 0.209 * wsu
        } else {
            1.1 + 0.715 * (wsu - 5.0).pow(0.844)
        }

        // Moisture function (fm)
        var fm = when {
            mcu < 12.0 -> exp(-0.108 * mcu)
            mcu < 20.0 && wsu < 10.0 -> 0.6838 - 0.0342 * mcu
            mcu < 23.9 && wsu >= 10.0 -> 0.547 - 0.0228 * mcu
            else -> 0.0
        }

        // Ensure fm is not negative
        if (fm < 0.0) {
            fm = 0.0
        }

        // Curing factor (using the previously converted function)
        val cf = curingFactor(cur `in` percent)

        return (fw * fm * cf) * (meters / minutes)
    }

    /**
     * Calculate Standing Grass Rate of Spread (ROS) based on Cheney (1998).
     *
     * @param ws  10m open wind speed (km/h)
     * @param mc  Moisture content in grass (%)
     * @param cur Percentage of grassland cured (%)
     * @return    Rate of Spread in m/min
     */
    private fun standingGrassSpreadRos(
        ws: Measure<Velocity>,
        mc: Measure<Probability>,
        cur: Measure<Probability>
    ): Measure<UnitsRatio<Length, Time>> {
        val wsu = ws `in` (kilometers / hours)
        val mcu = mc `in` percent
        // Wind function (fw) for standing grass
        val fw = 16.67 * if (wsu < 5.0) {
            0.054 + 0.269 * wsu
        } else {
            1.4 + 0.838 * (wsu - 5.0).pow(0.844)
        }

        // Moisture function (fm) - same logic as matted grass
        var fm = when {
            mcu < 12.0 -> exp(-0.108 * mcu)
            mcu < 20.0 && wsu < 10.0 -> 0.6838 - 0.0342 * mcu
            mcu < 23.9 && wsu >= 10.0 -> 0.547 - 0.0228 * mcu
            else -> 0.0
        }

        // Ensure fm is not negative
        if (fm < 0.0) {
            fm = 0.0
        }

        // Curing factor
        val cf = curingFactor(cur `in` percent)

        return (fw * fm * cf) * (meters / minutes)
    }

    /**
     * Calculate Grassland Spread Index (GSI)
     *
     * @param ws        Wind Speed (km/h)
     * @param mc        Grass moisture content (percent)
     * @param cur       Degree of curing (percent, 0-100)
     * @param standing  Whether the grass is standing (true) or matted (false)
     * @return          Grassland Spread Index
     */
    operator fun invoke(
        ws: Measure<Velocity>,
        mc: Measure<Probability>,
        cur: Measure<Probability>,
        standing: Boolean
    ): Double {
        // Select the appropriate Rate of Spread model based on grass state
        val ros = if (standing) {
            standingGrassSpreadRos(ws, mc, cur)
        } else {
            mattedGrassSpreadRos(ws, mc, cur)
        }

        // Scale the ROS to determine the index value
        return 1.11 * (ros `in` (meters / minutes))
    }
}