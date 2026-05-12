package cloud.mallne.geokit.fwi.calculator

import cloud.mallne.geokit.fwi.calculator.indices.FireWeatherIndex
import cloud.mallne.geokit.fwi.calculator.indices.GrasslandSpreadIndex
import cloud.mallne.geokit.fwi.calculator.indices.InitialSpreadIndex
import cloud.mallne.geokit.fwi.model.DailySummary
import cloud.mallne.geokit.fwi.model.WeatherRow
import co.touchlab.kermit.Logger
import kotlinx.datetime.LocalDate
import kotlin.math.pow

object DailySummaries {
    private val log = Logger.withTag("DailySummaries")
    private const val SPREAD_THRESHOLD_ISI = 5.0

    private fun smooth5Pt(source: List<Double>): List<Double> {
        val cap = source.size
        val dest = MutableList<Double?>(cap) { null }

        dest[0] = source[0]
        dest[cap - 1] = source[cap - 1]

        var miss = 0
        for (i in 0..2) {
            if (source[i] < -90.0) miss++
        }
        dest[1] = if (miss == 0) {
            0.25 * source[0] + 0.5 * source[1] + 0.25 * source[2]
        } else {
            source[1]
        }

        for (i in 2 until cap - 2) {
            miss = 0
            for (j in (i - 2)..(i + 2)) {
                if (source[j] < -90.0) miss++
            }
            dest[i] = if (miss == 0) {
                (1.0 / 16.0 * source[i - 2]) + (4.0 / 16.0 * source[i - 1]) +
                        (6.0 / 16.0 * source[i]) + (4.0 / 16.0 * source[i + 1]) +
                        (1.0 / 16.0 * source[i + 2])
            } else {
                source[i]
            }
        }

        miss = 0
        for (i in (cap - 3)..(cap - 1)) {
            if (source[i] < -90.0) miss++
        }
        dest[cap - 2] = if (miss == 0) {
            0.25 * source[cap - 3] + 0.5 * source[cap - 2] + 0.25 * source[cap - 1]
        } else {
            source[cap - 2]
        }

        return dest.map { it!! }
    }

    private fun pseudoDate(date: LocalDate, hr: Int, resetHr: Int = 5): String {
        val adjustedJd = if (hr < resetHr) {
            date.dayOfYear - 1
        } else {
            date.dayOfYear
        }

        val (adjustedYr, adjustedJdFinal) = if (adjustedJd == 0) {
            val prevYear = if (isLeapYear(date.year - 1)) 366 else 365
            date.year - 1 to prevYear
        } else {
            date.year to adjustedJd
        }

        return "$adjustedYr-$adjustedJdFinal"
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    operator fun invoke(
        hourlyFWI: List<WeatherRow.Processed>,
        resetHr: Int = 5,
        silent: Boolean = false,
        roundOut: Int? = 4
    ): List<DailySummary> {
        if (!silent) {
            log.i("\n########\nFWI2025: Daily Summaries\n")
        }

        val hourlyData = hourlyFWI.toMutableList()

        val hadId = hourlyData.any { it.id != null }

        if (!hadId) {
            if (hourlyData.map { it.date.year }.distinct().size == 1 &&
                hourlyData.map { it.lat }.distinct().size == 1 &&
                hourlyData.map { it.long }.distinct().size == 1
            ) {
                hourlyData.forEachIndexed { index, row ->
                    hourlyData[index] = row.copy(id = "stn")
                }
            } else {
                error("Missing \"id\" column with multiple years and locations in data")
            }
        }

        val groupedById = hourlyData.groupBy { it.id }

        val results = mutableListOf<DailySummary>()

        for ((stn, byStn) in groupedById) {
            if (!silent) {
                log.i("Summarizing $stn to daily")
            }

            val withPseudoDate = byStn.map { row ->
                row to pseudoDate(row.date, row.hr, resetHr)
            }

            val dateGrassStanding =
                LocalDate(byStn.first().date.year, FireWeatherIndex.MON_STANDING, FireWeatherIndex.DAY_STANDING)

            val groupedByPseudoDate = withPseudoDate.groupBy { it.second }

            for ((_, byDate) in groupedByPseudoDate) {
                val rows = byDate.map { it.first }

                if (rows.size <= 12) continue

                val wsValues = rows.map { it.ws }
                val wsSmooth = smooth5Pt(wsValues)

                val isiSmooth = rows.mapIndexed { index, row ->
                    InitialSpreadIndex(wsSmooth[index], row.ffmc)
                }

                val maxFfmc = rows.maxOf { it.ffmc }
                val peakTime = if (maxFfmc < 85.0) 12 else isiSmooth.indexOf(isiSmooth.maxOrNull()!!)

                val rowAtPeak = rows[peakTime]

                val sr = rowAtPeak.sunrise ?: 0.0
                val ss = rowAtPeak.sunset ?: 0.0
                val sunriseFormatted = "${sr.toInt().toString().padStart(2, '0')}:${
                    (60 * (sr - sr.toInt())).toInt().toString().padStart(2, '0')
                }"
                val sunsetFormatted = "${ss.toInt().toString().padStart(2, '0')}:${
                    (60 * (ss - ss.toInt())).toInt().toString().padStart(2, '0')
                }"

                val duration = if (isiSmooth.any { it >= SPREAD_THRESHOLD_ISI }) {
                    val activeBurning = rows.filterIndexed { index, _ -> isiSmooth[index] >= SPREAD_THRESHOLD_ISI }
                    val firstHour = activeBurning.first().date.toEpochDays() * 24 + activeBurning.first().hr
                    val lastHour = activeBurning.last().date.toEpochDays() * 24 + activeBurning.last().hr
                    (lastHour - firstHour) + 1
                } else {
                    0
                }

                val d = byDate[0].first.date
                val standing: Boolean
                val mcgfmc: Double

                if (FireWeatherIndex.GRASS_TRANSITION && d < dateGrassStanding) {
                    standing = false
                    mcgfmc = rowAtPeak.mcgfmcMatted
                } else {
                    standing = true
                    mcgfmc = rowAtPeak.mcgfmcStanding
                }

                val gsiSmoothVal = GrasslandSpreadIndex(
                    wsSmooth[peakTime],
                    mcgfmc,
                    rowAtPeak.percentCured ?: 0.0,
                    standing
                )

                results.add(
                    DailySummary(
                        id = stn,
                        date = byDate[0].first.date,
                        sunrise = sunriseFormatted,
                        sunset = sunsetFormatted,
                        peakHr = rowAtPeak.hr,
                        duration = duration.toInt(),
                        ffmc = rowAtPeak.ffmc,
                        dmc = rowAtPeak.dmc,
                        dc = rowAtPeak.dc,
                        isi = rowAtPeak.isi,
                        bui = rowAtPeak.bui,
                        fwi = rowAtPeak.fwi,
                        dsr = rowAtPeak.dsr,
                        gfmc = rowAtPeak.gfmc,
                        gsi = rowAtPeak.gsi,
                        gfwi = rowAtPeak.gfwi,
                        wsSmooth = wsSmooth[peakTime],
                        isiSmooth = isiSmooth[peakTime],
                        gsiSmooth = gsiSmoothVal
                    )
                )
            }
        }

        val outResults = if (hadId) results else results.map { it.copy(id = null) }

        val roundedResults = if (roundOut != null && roundOut >= 0) {
            outResults.map { summary ->
                val factor = 10.0.pow(roundOut)
                summary.copy(
                    ffmc = (summary.ffmc * factor).toInt() / factor,
                    dmc = (summary.dmc * factor).toInt() / factor,
                    dc = (summary.dc * factor).toInt() / factor,
                    isi = (summary.isi * factor).toInt() / factor,
                    bui = (summary.bui * factor).toInt() / factor,
                    fwi = (summary.fwi * factor).toInt() / factor,
                    dsr = (summary.dsr * factor).toInt() / factor,
                    gfmc = (summary.gfmc * factor).toInt() / factor,
                    gsi = (summary.gsi * factor).toInt() / factor,
                    gfwi = (summary.gfwi * factor).toInt() / factor,
                    wsSmooth = (summary.wsSmooth * factor).toInt() / factor,
                    isiSmooth = (summary.isiSmooth * factor).toInt() / factor,
                    gsiSmooth = (summary.gsiSmooth * factor).toInt() / factor
                )
            }
        } else {
            outResults
        }

        if (!silent) {
            log.i("########\n")
        }

        return roundedResults
    }
}