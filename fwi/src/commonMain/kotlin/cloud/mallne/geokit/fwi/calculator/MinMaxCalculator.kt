package cloud.mallne.geokit.fwi.calculator

import cloud.mallne.geokit.fwi.calculator.Util.findQ
import cloud.mallne.geokit.fwi.calculator.Util.findRh
import cloud.mallne.geokit.fwi.model.MinMaxWeather
import cloud.mallne.geokit.fwi.model.WeatherRow
import cloud.mallne.geokit.fwi.model.WeatherRowConstants
import cloud.mallne.units.Length.Companion.millimeters
import cloud.mallne.units.Measure
import cloud.mallne.units.Probability
import cloud.mallne.units.Probability.Companion.percent
import cloud.mallne.units.Temperature
import cloud.mallne.units.Temperature.Companion.celsius
import cloud.mallne.units.times
import co.touchlab.kermit.Logger
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object MinMaxCalculator {
    private val log = Logger.withTag("MinMaxCalculator")

    private fun tempMinMax(
        tempNoon: Measure<Temperature>,
        rhNoon: Measure<Probability>
    ): Pair<Measure<Temperature>, Measure<Temperature>> {
        val tempNoonU = tempNoon `in` celsius
        val rhNoonU = rhNoon `in` percent
        val tempRange = 17.0 - 0.16 * rhNoonU + 0.22 * tempNoonU
        return if (tempRange <= 2.0) {
            val tempMax = tempNoonU + 1.0
            val tempMin = tempNoonU - 1.0
            tempMin * celsius to tempMax * celsius
        } else {
            val tempMax = tempNoonU + 2.0
            val tempMin = tempMax - tempRange
            tempMin * celsius to tempMax * celsius
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
            val rhMin = min(100.0, max(0.0, rhMinUnclamped `in` WeatherRowConstants.rh))
            val rhMaxUnclamped = findRh(q, tempMin)
            val rhMax = min(100.0, max(0.0, rhMaxUnclamped `in` WeatherRowConstants.rh))
            val wsMin = 0.15 * row.ws
            val wsMax = 1.25 * row.ws

            if (roundOut != null && roundOut >= 0) {
                val factor = 10.0.pow(roundOut)
                MinMaxWeather(
                    id = row.id,
                    date = row.date,
                    tempMin = (((tempMin `in` WeatherRowConstants.temp) * factor).toInt() / factor) * WeatherRowConstants.temp,
                    tempMax = (((tempMax `in` WeatherRowConstants.temp) * factor).toInt() / factor) * WeatherRowConstants.temp,
                    rhMin = ((rhMin * factor).toInt() / factor) * WeatherRowConstants.rh,
                    rhMax = ((rhMax * factor).toInt() / factor) * WeatherRowConstants.rh,
                    wsMin = (((wsMin `in` WeatherRowConstants.ws) * factor).toInt() / factor) * WeatherRowConstants.ws,
                    wsMax = (((wsMax `in` WeatherRowConstants.ws) * factor).toInt() / factor) * WeatherRowConstants.ws,
                    prec = row.prec
                )
            } else {
                MinMaxWeather(
                    id = row.id,
                    date = row.date,
                    tempMin = tempMin,
                    tempMax = tempMax,
                    rhMin = rhMin * WeatherRowConstants.rh,
                    rhMax = rhMax * WeatherRowConstants.rh,
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

    fun aggregate(hourlyInput: List<WeatherRow.Input>): List<MinMaxWeather> {
        return hourlyInput.groupBy { it.date }.map { (date, rows) ->
            val tempMin = rows.minOf { it.temp }
            val tempMax = rows.maxOf { it.temp }
            val rhMin = rows.minOf { it.rh }
            val rhMax = rows.maxOf { it.rh }
            val wsMin = rows.minOf { it.ws }
            val wsMax = rows.maxOf { it.ws }
            val prec = rows.fold(0.0 * millimeters) { current, row-> row.prec + current }
            MinMaxWeather(
                date = date,
                tempMin = tempMin,
                tempMax = tempMax,
                rhMin = rhMin,
                rhMax = rhMax,
                wsMin = wsMin,
                wsMax = wsMax,
                prec = prec,
                id = hourlyInput.firstOrNull()?.id,
            )
        }
    }
}