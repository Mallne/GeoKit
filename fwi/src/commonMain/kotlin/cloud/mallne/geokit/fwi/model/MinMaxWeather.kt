package cloud.mallne.geokit.fwi.model

import kotlinx.datetime.LocalDate

data class MinMaxWeather(
    val id: String?,
    val date: LocalDate,
    val tempMin: Double,
    val tempMax: Double,
    val rhMin: Double,
    val rhMax: Double,
    val wsMin: Double,
    val wsMax: Double,
    val prec: Double
)