package cloud.mallne.geokit.fwi.calculator.indices

import kotlin.math.ln

object GrasslandFireWeatherIndex {
    /**
     * Calculate Grassland Fire Weather Index (GFWI)
     *
     * @param gsi   Grassland Spread Index
     * @param load  Grassland Fuel Load (kg/m^2)
     * @return      Grassland Fire Weather Index
     */
    operator fun invoke(gsi: Double, load: Double): Double {
        // Convert the index back to Rate of Spread (m/min)
        val ros = gsi / 1.11

        // Calculate Fire Line Intensity (kW/m)
        val fint = 300.0 * load * ros

        return if (fint > 100.0) {
            // Logarithmic scale for higher intensities
            ln(fint / 60.0) / 0.14
        } else {
            // Linear scale for lower intensities
            fint / 25.0
        }
    }
}