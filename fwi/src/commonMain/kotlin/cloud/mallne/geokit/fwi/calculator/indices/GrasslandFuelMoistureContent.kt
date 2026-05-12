package cloud.mallne.geokit.fwi.calculator.indices

import cloud.mallne.geokit.fwi.calculator.Util.curingFactor
import cloud.mallne.geokit.fwi.calculator.Util.pign
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

object GrasslandFuelMoistureContent {
    internal const val DEFAULT_GRASS_FUEL_LOAD = 0.35

    /**
     * Calculate hourly grassland fuel moisture content.
     *
     * @param lastmc          Previous grassland fuel moisture content (%)
     * @param temp            Temperature (Celsius)
     * @param rh              Relative Humidity (%, 0-100)
     * @param ws              Wind Speed (km/h)
     * @param rain            Rainfall (mm)
     * @param solrad          Solar radiation (kW/m^2)
     * @param load            Grassland Fuel Load (kg/m^2)
     * @param timeIncrement   Duration of timestep (hr, default 1.0)
     * @return                Grassland fuel moisture content (%)
     */
    operator fun invoke(
        lastmc: Double,
        temp: Double,
        rh: Double,
        ws: Double,
        rain: Double,
        solrad: Double,
        load: Double,
        timeIncrement: Double = 1.0
    ): Double {
        val rf = 0.27
        val drf = 0.389633

        var mo = lastmc

        // 1. Wetting from rain
        if (rain != 0.0) {
            mo += (rain / load) * 100.0
            if (mo > 250.0) mo = 250.0
        }

        // 2. Fuel Temperature (tf) from CEVW
        val tf = temp + 17.9 * solrad * exp(-0.034 * ws)

        // 3. Fuel Humidity (rhf)
        val rhf = if (tf > temp) {
            val numerator = rh * 6.107 * 10.0.pow(7.5 * temp / (temp + 237.0))
            val denominator = 6.107 * 10.0.pow(7.5 * tf / (tf + 237.0))
            numerator / denominator
        } else {
            rh
        }

        // Common factor in Equilibrium Moisture Content (EMC) formulas
        val e1 = rf * (26.7 - tf) * (1.0 - (1.0 / exp(0.115 * rhf)))

        // 4. Grass EMC (Drying and Wetting)
        val ed = 1.62 * rhf.pow(0.532) + (13.7 * exp((rhf - 100.0) / 13.0)) + e1
        val ew = 1.42 * rhf.pow(0.512) + (12.0 * exp((rhf - 100.0) / 18.0)) + e1

        val moed = mo - ed
        val moew = mo - ew

        val m: Double

        // 5. Equilibrium Check
        if (moed == 0.0 || (moew >= 0.0 && moed < 0.0)) {
            m = mo
        } else {
            val e: Double
            val moe: Double
            var a1: Double

            if (moed > 0.0) {
                // Drying
                a1 = rhf / 100.0
                e = ed
                moe = moed
            } else {
                // Wetting
                a1 = (100.0 - rhf) / 100.0
                e = ew
                moe = moew
            }

            // Clamp a1 to avoid complex numbers in power calculation
            if (a1 < 0.0) a1 = 0.0

            // 6. Calculate rate of change (xkd)
            var xkd = (0.424 * (1.0 - a1.pow(1.7)) + (0.0694 * sqrt(ws) * (1.0 - a1.pow(8.0))))
            xkd *= drf * exp(0.0365 * tf)

            // 7. Final moisture calculation
            // log(10.0) is the natural log of 10 (~2.302585)
            m = e + moe * exp(-1.0 * ln(10.0) * xkd * timeIncrement)
        }

        return m
    }

    /**
     * Converts cured grassland moisture to a GFMC code value.
     * Accounts for curing effects and wind speed to calculate an "effective" moisture content.
     *
     * @param mc    Moisture content (%)
     * @param cur   Percent curing of the grassland (%)
     * @param wind  10m open wind speed (km/h)
     * @return      Grassland Fuel Moisture Code (GFMC) value
     */
    internal fun mcgfmcToGfmc(mc: Double, cur: Double, wind: Double): Double {
        val wind2mOpenFactor = 0.75

        // Grass coefficients for PsusF model
        val intercept = 1.49
        val cMoisture = -0.11
        val cWind = 0.075

        // Convert 10m wind to 2m wind
        val wind2m = wind2mOpenFactor * wind

        // Calculate initial probability of ignition
        val probIgn = pign(mc, wind2m, intercept, cMoisture, cWind)

        // Adjust ignition based on the curing factor (Cruz et al 2015)
        val newPign = curingFactor(cur) * probIgn

        // Algebraically reverse the Pign equation to find effective moisture content (egmc)
        var egmc = if (newPign > 0.0 && newPign < 1.0) {
            // Logit function reversal
            ((ln(newPign / (1.0 - newPign)) - intercept - (cWind * wind2m)) / cMoisture)
        } else {
            250.0
        }

        // Apply saturation limit
        if (egmc > 250.0) {
            egmc = 250.0
        } else if (egmc < 0.0) {
            // Safety check for very high ignition probabilities
            egmc = 0.0
        }

        // Convert effective moisture content to FFMC scale
        return FineFuelMoistureContent.mcffmcToFfmc(egmc)
    }
}