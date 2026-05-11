package cloud.mallne.geokit.fwi.calculator

import cloud.mallne.geokit.fwi.calculator.Util.curingFactor
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
    fun mattedGrassSpreadRos(ws: Double, mc: Double, cur: Double): Double {
        // Wind function (fw)
        val fw = 16.67 * if (ws < 5.0) {
            0.054 + 0.209 * ws
        } else {
            1.1 + 0.715 * (ws - 5.0).pow(0.844)
        }

        // Moisture function (fm)
        var fm = when {
            mc < 12.0 -> exp(-0.108 * mc)
            mc < 20.0 && ws < 10.0 -> 0.6838 - 0.0342 * mc
            mc < 23.9 && ws >= 10.0 -> 0.547 - 0.0228 * mc
            else -> 0.0
        }

        // Ensure fm is not negative
        if (fm < 0.0) {
            fm = 0.0
        }

        // Curing factor (using the previously converted function)
        val cf = curingFactor(cur)

        return fw * fm * cf
    }

    /**
     * Calculate Standing Grass Rate of Spread (ROS) based on Cheney (1998).
     *
     * @param ws  10m open wind speed (km/h)
     * @param mc  Moisture content in grass (%)
     * @param cur Percentage of grassland cured (%)
     * @return    Rate of Spread in m/min
     */
    fun standingGrassSpreadRos(ws: Double, mc: Double, cur: Double): Double {
        // Wind function (fw) for standing grass
        val fw = 16.67 * if (ws < 5.0) {
            0.054 + 0.269 * ws
        } else {
            1.4 + 0.838 * (ws - 5.0).pow(0.844)
        }

        // Moisture function (fm) - same logic as matted grass
        var fm = when {
            mc < 12.0 -> exp(-0.108 * mc)
            mc < 20.0 && ws < 10.0 -> 0.6838 - 0.0342 * mc
            mc < 23.9 && ws >= 10.0 -> 0.547 - 0.0228 * mc
            else -> 0.0
        }

        // Ensure fm is not negative
        if (fm < 0.0) {
            fm = 0.0
        }

        // Curing factor
        val cf = curingFactor(cur)

        return fw * fm * cf
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
        ws: Double,
        mc: Double,
        cur: Double,
        standing: Boolean
    ): Double {
        // Select the appropriate Rate of Spread model based on grass state
        val ros = if (standing) {
            standingGrassSpreadRos(ws, mc, cur)
        } else {
            mattedGrassSpreadRos(ws, mc, cur)
        }

        // Scale the ROS to determine the index value
        return 1.11 * ros
    }
}