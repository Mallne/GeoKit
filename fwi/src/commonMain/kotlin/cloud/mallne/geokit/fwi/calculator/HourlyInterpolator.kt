package cloud.mallne.geokit.fwi.calculator

import cloud.mallne.geokit.fwi.model.MinMaxWeather
import cloud.mallne.geokit.fwi.model.WeatherRow
import co.touchlab.kermit.Logger
import kotlinx.datetime.LocalDate
import kotlin.math.*

object HourlyInterpolator {
    private val log = Logger.withTag("HourlyInterpolator")

    private data class PredictionCoefficients(
        val cAlpha: Double,
        val cBeta: Double,
        val cGamma: Double
    )

    private val C_TEMP = PredictionCoefficients(0.0, 2.75, -1.9)
    private val C_RH = PredictionCoefficients(0.25, 2.75, -2.0)
    private val C_WIND = PredictionCoefficients(1.0, 1.5, -1.3)

    private data class DayPrediction(
        val date: LocalDate,
        val sunrise: Double,
        val sunset: Double,
        val solarNoon: Double,
        val tempMin: Double,
        val tempMax: Double,
        val rhMin: Double,
        val rhMax: Double,
        val wsMin: Double,
        val wsMax: Double,
        val prec: Double
    )

    private fun calculateSunriseSunset(
        lat: Double,
        long: Double,
        timezone: Double,
        date: LocalDate
    ): Pair<Double, Double> {
        val latRad = lat * PI / 180.0
        val isLeap = (date.year % 4 == 0 && date.year % 100 != 0) || (date.year % 400 == 0)
        val daysInYear = if (isLeap) 366.0 else 365.0
        val jd = date.dayOfYear.toDouble()

        val fracyear = (2.0 * PI / daysInYear) * (jd - 1.0 + (12.0 - 12.0) / 24.0)

        val eqtime = 229.18 * (0.000075 +
                0.001868 * cos(fracyear) - 0.032077 * sin(fracyear) -
                0.014615 * cos(2.0 * fracyear) - 0.040849 * sin(2.0 * fracyear))

        val decl = 0.006918 -
                0.399912 * cos(fracyear) + 0.070257 * sin(fracyear) -
                0.006758 * cos(fracyear * 2.0) + 0.000907 * sin(2.0 * fracyear) -
                0.002697 * cos(3.0 * fracyear) + 0.00148 * sin(3.0 * fracyear)

        val zenithRad = 90.833 * PI / 180.0
        var xTmp = cos(zenithRad) / (cos(latRad) * cos(decl)) - tan(latRad) * tan(decl)
        xTmp = max(-1.0, min(1.0, xTmp))
        val halfDayDegree = acos(xTmp) * 180.0 / PI

        val sunrise = (720.0 - 4.0 * (long + halfDayDegree) - eqtime) / 60.0 + timezone
        val sunset = (720.0 - 4.0 * (long - halfDayDegree) - eqtime) / 60.0 + timezone

        return sunrise to sunset
    }

    private fun makeVariablePrediction(
        time: Double,
        varMin: Double,
        varMax: Double,
        timeMin: Double,
        timeMax: Double,
        changeAt: Double,
        cGamma: Double,
        varMinTomorrow: Double,
        minValue: Double,
        maxValue: Double
    ): Double {
        val isRising = time < changeAt

        return if (isRising) {
            val fOrG = (time + 24.0 - timeMin) / (timeMax - timeMin)
            val value = varMin + (varMax - varMin) * sin((PI / 2.0) * fOrG)
            max(minValue, min(maxValue, value))
        } else {
            val timeGMin = timeMin
            val varGMin = varMinTomorrow
            val varChange = varMin + (varMax - varMin) * sin((PI / 2.0) * ((changeAt - timeMin) / (timeMax - timeMin)))
            val fOrG = (time + 24.0 - changeAt) / (24.0 - changeAt + timeGMin)
            val value = varGMin + (varChange - varGMin) * exp(cGamma * fOrG)
            max(minValue, min(maxValue, value))
        }
    }

    private fun predictVariableForDay(
        prediction: DayPrediction,
        nextPrediction: DayPrediction?,
        varMinKey: (DayPrediction) -> Double,
        varMaxKey: (DayPrediction) -> Double,
        timeMinOffset: Double,
        timeMaxOffset: Double,
        c: PredictionCoefficients,
        minValue: Double,
        maxValue: Double
    ): List<Double> {
        val results = mutableListOf<Double>()
        val varMinTomorrow = nextPrediction?.let { varMinKey(it) } ?: varMinKey(prediction)

        for (hour in 0..23) {
            val time = hour.toDouble()
            val value = makeVariablePrediction(
                time = time,
                varMin = varMinKey(prediction),
                varMax = varMaxKey(prediction),
                timeMin = prediction.sunrise + timeMinOffset,
                timeMax = prediction.solarNoon + timeMaxOffset,
                changeAt = prediction.sunset,
                cGamma = c.cGamma,
                varMinTomorrow = varMinTomorrow,
                minValue = minValue,
                maxValue = maxValue
            )
            results.add(value)
        }

        return results
    }

    operator fun invoke(
        dailyInput: List<MinMaxWeather>,
        lat: Double? = null,
        long: Double? = null,
        timezone: Double? = null,
        precHr: Int? = null,
        skipInvalid: Boolean = false,
        silent: Boolean = false,
        roundOut: Int? = 4
    ): List<WeatherRow.Input> {
        if (!silent) {
            log.i("\n########\nFWI2025: Make Hourly Inputs\n")
        }

        if (dailyInput.isEmpty()) {
            if (!silent) log.i("########\n")
            return emptyList()
        }

        val latValue = lat ?: 0.0
        val longValue = long ?: 0.0
        val timezoneValue = timezone ?: 0.0

        val groupedById = dailyInput.groupBy { it.id }

        val allResults = mutableListOf<WeatherRow.Input>()

        for ((stn, byStn) in groupedById) {
            val sortedDays = byStn.sortedBy { it.date }

            val predictions = sortedDays.map { mm ->
                val (sr, ss) = calculateSunriseSunset(latValue, longValue, timezoneValue, mm.date)
                val solarNoon = (ss - sr) / 2.0 + sr
                DayPrediction(
                    date = mm.date,
                    sunrise = sr,
                    sunset = ss,
                    solarNoon = solarNoon,
                    tempMin = mm.tempMin,
                    tempMax = mm.tempMax,
                    rhMin = mm.rhMin,
                    rhMax = mm.rhMax,
                    wsMin = mm.wsMin,
                    wsMax = mm.wsMax,
                    prec = mm.prec
                )
            }

            for (i in predictions.indices) {
                val pred = predictions[i]
                val nextPred = predictions.getOrNull(i + 1)

                val tempHourly = predictVariableForDay(
                    pred, nextPred,
                    { it.tempMin }, { it.tempMax },
                    C_TEMP.cAlpha, C_TEMP.cBeta, C_TEMP,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY
                )
                val windHourly = predictVariableForDay(
                    pred, nextPred,
                    { it.wsMin }, { it.wsMax },
                    C_WIND.cAlpha, C_WIND.cBeta, C_WIND,
                    0.0, Double.POSITIVE_INFINITY
                )
                val rhOppHourly = predictVariableForDay(
                    pred, nextPred,
                    { 1.0 - it.rhMax }, { 1.0 - it.rhMin },
                    C_RH.cAlpha, C_RH.cBeta, C_RH,
                    0.0, 1.0
                )

                for (hour in 0..23) {
                    val temp = tempHourly[hour]
                    val ws = windHourly[hour]
                    val rhOpp = rhOppHourly[hour]
                    val rh = 100.0 * (1.0 - rhOpp)

                    val precPlacement = precHr ?: ceil(pred.sunrise).toInt().coerceIn(0, 23)
                    val prec = if (hour == precPlacement) pred.prec else 0.0

                    val roundedTemp = if (roundOut != null && roundOut >= 0) {
                        val factor = 10.0.pow(roundOut)
                        (temp * factor).toInt() / factor
                    } else temp

                    val roundedRh = if (roundOut != null && roundOut >= 0) {
                        val factor = 10.0.pow(roundOut)
                        (rh * factor).toInt() / factor
                    } else rh

                    val roundedWs = if (roundOut != null && roundOut >= 0) {
                        val factor = 10.0.pow(roundOut)
                        (ws * factor).toInt() / factor
                    } else ws

                    allResults.add(
                        WeatherRow.Input(
                            id = stn,
                            date = pred.date,
                            hr = hour,
                            temp = roundedTemp,
                            rh = roundedRh,
                            ws = roundedWs,
                            prec = prec,
                            lat = latValue,
                            long = longValue,
                            timezone = timezoneValue
                        )
                    )
                }
            }
        }

        if (!silent) {
            log.i("########\n")
        }

        return allResults
    }
}