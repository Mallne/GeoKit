package cloud.mallne.geokit.fwi.calculator.indices

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
    operator fun invoke(wind: Double, ffmc: Double): Double {
        val fm = FineFuelMoistureContent.ffmcToMcffmc(ffmc)
        val fw = if (wind >= 40.0) 12.0 * (1.0 - exp(-0.0818 * (wind - 28.0))) else exp(0.05039 * wind)
        val ff = 91.9 * exp(-0.1386 * fm) * (1.0 + fm.pow(5.31) / 4.93e07)
        return 0.208 * fw * ff
    }
}