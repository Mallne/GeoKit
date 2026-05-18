package cloud.mallne.geokit.fwi.calculator.indices

import cloud.mallne.units.Length.Companion.kilometers
import cloud.mallne.units.Measure
import cloud.mallne.units.Probability.Companion.percent
import cloud.mallne.units.Time.Companion.hours
import cloud.mallne.units.Velocity
import cloud.mallne.units.div
import cloud.mallne.units.times
import kotlin.math.exp
import kotlin.math.pow

object InitialSpreadIndex {
    /**
     * Calculate Initial Spread Index (ISI)
     *
     * @param wind [Double]           Wind Speed (km/h)
     * @param ffmc [Double]           Fine Fuel Moisure Code
     * @return     [Double]           Initial Spread Index
     */
    operator fun invoke(wind: Measure<Velocity>, ffmc: Double): Double {
        val fm = FineFuelMoistureContent.ffmcToMcffmc(ffmc) `in` percent
        val fw =
            if (wind >= 40.0 * (kilometers / hours)) 12.0 * (1.0 - exp(-0.0818 * ((wind `in` (kilometers / hours)) - 28.0))) else exp(
                0.05039 * (wind `in` (kilometers / hours))
            )
        val ff = 91.9 * exp(-0.1386 * fm) * (1.0 + fm.pow(5.31) / 4.93e07)
        return 0.208 * fw * ff
    }
}