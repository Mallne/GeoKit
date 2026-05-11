package cloud.mallne.geokit.fwi.calculator

import cloud.mallne.geokit.fwi.model.CanopyState
import cloud.mallne.geokit.fwi.model.WeatherRow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlin.math.*

object Util {
    fun findQ(temp: Double, rh: Double): Double {
        val svp = 6.108 * exp(17.27 * temp / (temp + 237.3))
        val vp = svp * rh / 100.0
        return 217.0 * vp / (273.17 + temp)
    }

    fun findRh(q: Double, temp: Double): Double {
        val curVp = (273.17 + temp) * q / 217.0
        val rh = 100.0 * curVp / (6.108 * exp(17.27 * temp / (temp + 237.3)))
        return rh
    }

    /**
     * Calculates sunrise, sunset, and solar radiation for the weather stream.
     * Mirrors the Python implementation's astronomical math for JD, EqTime, and Declination.
     */
    fun getSunlight(
        weatherData: List<WeatherRow>,
        getSolrad: Boolean = false
    ): List<WeatherRow> {
        return weatherData.map { row ->
            val latRad = row.lat * PI / 180.0

            // 1. Calculate Julian Day (day of year)
            val jd = row.date.dayOfYear.toDouble()
            val isLeap = isLeapYear(row.date.year)
            val daysInYear = if (isLeap) 366.0 else 365.0

            // 2. Fractional Year (fracyear) in radians
            // Python: 2.0 * pi * (jd - 1.0 + (dec_hour - 12.0) / 24.0) / daysInYear
            val fracyear = (2.0 * PI / daysInYear) * (jd - 1.0 + (row.hr - 12.0) / 24.0)

            // 3. Equation of Time (eqtime) in minutes
            val eqtime = 229.18 * (0.000075 +
                    0.001868 * cos(fracyear) - 0.032077 * sin(fracyear) -
                    0.014615 * cos(2.0 * fracyear) - 0.040849 * sin(2.0 * fracyear))

            // 4. Solar Declination (decl) in radians
            val decl = 0.006918 -
                    0.399912 * cos(fracyear) + 0.070257 * sin(fracyear) -
                    0.006758 * cos(fracyear * 2.0) + 0.000907 * sin(2.0 * fracyear) -
                    0.002697 * cos(3.0 * fracyear) + 0.00148 * sin(3.0 * fracyear)

            // 5. Sunrise/Sunset Calculation
            val zenithRad = 90.833 * PI / 180.0

            // Intermediate temporary value (x_tmp)
            var xTmp = cos(zenithRad) / (cos(latRad) * cos(decl)) - tan(latRad) * tan(decl)
            xTmp = xTmp.coerceIn(-1.0, 1.0)

            val halfDayDegree = acos(xTmp) * 180.0 / PI

            // Sunrise/Sunset in decimal hours
            val sunrise = (720.0 - 4.0 * (row.long + halfDayDegree) - eqtime) / 60.0 + row.timezone
            val sunset = (720.0 - 4.0 * (row.long - halfDayDegree) - eqtime) / 60.0 + row.timezone

            // 6. Solar Radiation (solrad) - optional
            var calculatedSolrad = row.solrad
            if (getSolrad) {
                val timeOffset = eqtime + 4.0 * row.long - 60.0 * row.timezone
                // TST in minutes
                val tst = (row.hr * 60.0) + timeOffset
                val hourAngle = (tst / 4.0) - 180.0

                var zenith = acos(
                    sin(latRad) * sin(decl) +
                            cos(latRad) * cos(decl) * cos(hourAngle * PI / 180.0)
                )
                zenith = min(PI / 2.0, zenith)

                val cosZenith = cos(zenith)

                // Vapor Pressure Deficit (vpd)
                val vpd = 6.11 * (1.0 - row.rh / 100.0) * exp(17.29 * row.temp / (row.temp + 237.3))

                calculatedSolrad = cosZenith * 0.92 * (1.0 - exp(-0.22 * vpd))
                if (calculatedSolrad < 1e-4) calculatedSolrad = 0.0
            }

            // Return a copy with calculated values
            row.clone(
                sunrise = sunrise,
                sunset = sunset,
                solrad = calculatedSolrad
            )
        }
    }

    /**
     * Utility to check for leap year
     */
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    /**
     * Calculates the percent cured based on the Boreal Plains seasonal variation.
     * Uses linear interpolation between 10-day data points.
     */
    fun seasonalCuring(
        date: LocalDate,
        startMon: Int = 3,
        startDay: Int = 12
    ): Double {
        val percentCuredLookup = listOf(
            96.0, 95.0, 93.0, 92.0, 90.5, 88.4, 84.4, 78.1, 68.7, 50.3,
            32.9, 23.0, 22.0, 21.0, 20.0, 25.7, 35.0, 43.0, 49.8, 60.0,
            68.0, 72.0, 75.0, 78.9, 86.0, 96.0
        )

        // 1. Calculate the start date for the green-up in the current year
        val currentYearStart = LocalDate(date.year, startMon, startDay)

        // 2. Find the reference start date (current year or previous year)
        val referenceStartDate = if (date < currentYearStart) {
            LocalDate(date.year - 1, startMon, startDay)
        } else {
            currentYearStart
        }

        // 3. Calculate days elapsed since green-up start
        // Python: shift.days + 1
        val daysIn = referenceStartDate.daysUntil(date) + 1

        // 4. Determine phase and interpolate
        val maxInterpolationDays = (percentCuredLookup.size - 1) * 10

        return if (daysIn < maxInterpolationDays) {
            val index = daysIn / 10
            val perCur0 = percentCuredLookup[index]
            val perCur1 = percentCuredLookup[index + 1]

            val periodFrac = (daysIn % 10) / 10.0

            // Linear interpolation: y = y0 + (y1 - y0) * fraction
            perCur0 + (perCur1 - perCur0) * periodFrac
        } else {
            // Return the "winter" cured value (last item in list)
            percentCuredLookup.last()
        }
    }

    fun pign(
        mc: Double,
        wind2m: Double,
        cint: Double,
        cmc: Double,
        cws: Double
    ): Double {
        val z = cint + (cmc * mc) + (cws * wind2m)
        return 1.0 / (1.0 + exp(-1.0 * z))
    }

    fun curingFactor(cur: Double): Double {
        return if (cur >= 20.0) {
            1.036 / (1.0 + 103.989 * exp(-0.0996 * (cur - 20.0)))
        } else {
            0.0
        }
    }

    fun dryingUnits(): Double = 1.0

    /**
     * Determines if the canopy interception of rainfall should be reset.
     * Mirrors the Python logic for drying units and target thresholds.
     */
    fun rainSinceInterceptReset(
        rain: Double,
        currentState: CanopyState,
        dryingStep: Double = 1.0 // Matches drying_units() default
    ): CanopyState {

        val targetDryingSinceIntercept = 5.0

        // Logic: If it is currently raining, or if there was no previous rain,
        // we reset the drying counter.
        return if (rain > 0.0 || currentState.rainTotalPrev == 0.0) {
            currentState.copy(
                dryingSinceIntercept = 0.0
            )
        } else {
            // Increment drying
            val updatedDrying = currentState.dryingSinceIntercept + dryingStep

            if (updatedDrying >= targetDryingSinceIntercept) {
                // Reset criteria met: canopy is considered dry
                CanopyState(
                    rainTotalPrev = 0.0,
                    dryingSinceIntercept = 0.0
                )
            } else {
                // Continue drying phase
                currentState.copy(
                    dryingSinceIntercept = updatedDrying
                )
            }
        }
    }
}