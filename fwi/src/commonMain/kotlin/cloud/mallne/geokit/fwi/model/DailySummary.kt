package cloud.mallne.geokit.fwi.model

import cloud.mallne.units.Measure
import cloud.mallne.units.Velocity
import kotlinx.datetime.LocalDate
import kotlin.time.Duration

data class DailySummary(
        val id: String?,
        val date: LocalDate,
        val sunrise: String,
        val sunset: String,
        val peakHr: Int,
        val duration: Duration,
        val ffmc: Double,
        val dmc: Double,
        val dc: Double,
        val isi: Double,
        val bui: Double,
        val fwi: Double,
        val dsr: Double,
        val gfmc: Double,
        val gsi: Double,
        val gfwi: Double,
        val wsSmooth: Measure<Velocity>,
        val isiSmooth: Double,
        val gsiSmooth: Double
    )