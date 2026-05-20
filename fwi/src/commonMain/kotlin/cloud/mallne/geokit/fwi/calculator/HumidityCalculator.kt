package cloud.mallne.geokit.fwi.calculator

import cloud.mallne.units.*
import cloud.mallne.units.Pressure.Companion.hectopascal
import cloud.mallne.units.Probability.Companion.decimal
import cloud.mallne.units.Probability.Companion.percent
import cloud.mallne.units.Temperature.Companion.celsius
import kotlin.math.exp
import kotlin.math.ln

object HumidityCalculator {
    // Constants for the August-Roche-Magnus approximation
    private const val A = 17.625
    private val B = 243.04 * celsius // °C
    private val C = 6.1094 * hectopascal // hPa

    /**
     * Calculates the saturation vapor pressure (es).
     *
     * @param temperature Actual air temperature in Celsius (°C).
     * @return Saturation vapor pressure in hectopascals (hPa).
     */
    private fun calculateSaturationVaporPressure(temperature: Measure<Temperature>): Measure<Pressure> {
        return C * exp(((A * temperature) / ((B `in` celsius) + (temperature `in` celsius))) `in` celsius)
    }

    /**
     * Calculates the dew point temperature.
     *
     * @param temperature Actual air temperature in Celsius (°C).
     * @param relativeHumidity Relative humidity as a percentage (0.0 to 100.0).
     * @return Dew point temperature in Celsius (°C).
     */
    fun calculateDewPointTemperature(
        temperature: Measure<Temperature>,
        relativeHumidity: Measure<Probability>
    ): Measure<Temperature> {
        require((relativeHumidity `in` percent) in 0.0..100.0) {
            "Relative humidity must be between 0.0 and 100.0"
        }

        val rhDecimal = (relativeHumidity `in` percent) / 100.0

        // Calculate the gamma term
        val gamma = ln(rhDecimal) + (A * (temperature `in` celsius)) / ((B `in` celsius) + (temperature `in` celsius))

        return (B * gamma) / (A - gamma)
    }

    /**
     * Calculates the relative humidity.
     *
     * @param temperature Actual air temperature in Celsius (°C).
     * @param dewPoint Dew point temperature in Celsius (°C).
     * @return Relative humidity as a percentage (0.0 to 100.0).
     */
    fun calculateRelativeHumidity(
        temperature: Measure<Temperature>,
        dewPoint: Measure<Temperature>
    ): Measure<Probability> {
        require(dewPoint <= temperature) {
            "Dew point ($dewPoint °C) cannot be greater than the actual temperature ($temperature °C)."
        }

        // Actual vapor pressure is equivalent to the saturation vapor pressure at the dew point
        val actualVaporPressure = calculateSaturationVaporPressure(dewPoint)
        val saturationVaporPressure = calculateSaturationVaporPressure(temperature)

        return ((actualVaporPressure `in` hectopascal) / (saturationVaporPressure `in` hectopascal)) * decimal
    }
}