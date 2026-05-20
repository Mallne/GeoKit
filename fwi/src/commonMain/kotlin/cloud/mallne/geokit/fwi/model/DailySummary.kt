package cloud.mallne.geokit.fwi.model

import cloud.mallne.units.Measure
import cloud.mallne.units.Velocity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Duration

data class DailySummary(
    val id: String?,
    val date: LocalDate,
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val peakHr: Int,
    val duration: Duration,
    override val ffmc: Double,
    override val dmc: Double,
    override val dc: Double,
    override val isi: Double,
    override val bui: Double,
    override val fwi: Double,
    override val dsr: Double,
    override val gfmc: Double,
    override val gsi: Double,
    override val gfwi: Double,
    val wsSmooth: Measure<Velocity>,
    val isiSmooth: Double,
    val gsiSmooth: Double
    ): IndiceComponents