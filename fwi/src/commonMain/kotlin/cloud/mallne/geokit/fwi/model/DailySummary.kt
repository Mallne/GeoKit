package cloud.mallne.geokit.fwi.model

import kotlinx.datetime.LocalDate

data class DailySummary(
        val id: String?,
        val date: LocalDate,
        val sunrise: String,
        val sunset: String,
        val peakHr: Int,
        val duration: Int,
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
        val wsSmooth: Double,
        val isiSmooth: Double,
        val gsiSmooth: Double
    )