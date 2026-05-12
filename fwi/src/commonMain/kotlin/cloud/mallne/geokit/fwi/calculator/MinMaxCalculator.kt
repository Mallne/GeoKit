package cloud.mallne.geokit.fwi.calculator

import cloud.mallne.geokit.fwi.calculator.Util.findQ
import cloud.mallne.geokit.fwi.calculator.Util.findRh
import cloud.mallne.geokit.fwi.model.MinMaxWeather
import cloud.mallne.geokit.fwi.model.WeatherRow
import co.touchlab.kermit.Logger
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object MinMaxCalculator {
    private val log = Logger.withTag("MinMaxCalculator")

    private fun tempMinMax(tempNoon: Double, rhNoon: Double): Pair<Double, Double> {
        val tempRange = 17.0 - 0.16 * rhNoon + 0.22 * tempNoon
        return if (tempRange <= 2.0) {
            val tempMax = tempNoon + 1.0
            val tempMin = tempNoon - 1.0
            tempMin to tempMax
        } else {
            val tempMax = tempNoon + 2.0
            val tempMin = tempMax - tempRange
            tempMin to tempMax
        }
    }

    operator fun invoke(
        dailyNoonInput: List<WeatherRow.Input>,
        silent: Boolean = false,
        roundOut: Int? = 4
    ): List<MinMaxWeather> {
        if (!silent) {
            log.i("\n########\nFWI2025: Make Min/Max Inputs\n")
            log.i("Predicting daily min/max weather")
        }

        val results = dailyNoonInput.map { row ->
            val (tempMin, tempMax) = tempMinMax(row.temp, row.rh)
            val q = findQ(row.temp, row.rh)
            val rhMinUnclamped = findRh(q, tempMax)
            val rhMin = min(100.0, max(0.0, rhMinUnclamped))
            val rhMaxUnclamped = findRh(q, tempMin)
            val rhMax = min(100.0, max(0.0, rhMaxUnclamped))
            val wsMin = 0.15 * row.ws
            val wsMax = 1.25 * row.ws

            if (roundOut != null && roundOut >= 0) {
                val factor = 10.0.pow(roundOut)
                MinMaxWeather(
                    id = row.id,
                    date = row.date,
                    tempMin = (tempMin * factor).toInt() / factor,
                    tempMax = (tempMax * factor).toInt() / factor,
                    rhMin = (rhMin * factor).toInt() / factor,
                    rhMax = (rhMax * factor).toInt() / factor,
                    wsMin = (wsMin * factor).toInt() / factor,
                    wsMax = (wsMax * factor).toInt() / factor,
                    prec = row.prec
                )
            } else {
                MinMaxWeather(
                    id = row.id,
                    date = row.date,
                    tempMin = tempMin,
                    tempMax = tempMax,
                    rhMin = rhMin,
                    rhMax = rhMax,
                    wsMin = wsMin,
                    wsMax = wsMax,
                    prec = row.prec
                )
            }
        }

        if (!silent) {
            log.i("########\n")
        }

        return results
    }
}