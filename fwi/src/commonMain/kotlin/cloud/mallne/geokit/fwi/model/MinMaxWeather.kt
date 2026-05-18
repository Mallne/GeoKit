package cloud.mallne.geokit.fwi.model

import cloud.mallne.units.Length
import cloud.mallne.units.Measure
import cloud.mallne.units.Probability
import cloud.mallne.units.Temperature
import cloud.mallne.units.Velocity
import kotlinx.datetime.LocalDate

data class MinMaxWeather(
    val id: String?,
    val date: LocalDate,
    val tempMin: Measure<Temperature>,
    val tempMax: Measure<Temperature>,
    val rhMin: Measure<Probability>,
    val rhMax: Measure<Probability>,
    val wsMin: Measure<Velocity>,
    val wsMax: Measure<Velocity>,
    val prec: Measure<Length>
)