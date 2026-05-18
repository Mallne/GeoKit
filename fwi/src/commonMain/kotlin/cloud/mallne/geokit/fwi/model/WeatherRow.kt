package cloud.mallne.geokit.fwi.model

import cloud.mallne.units.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.UtcOffset
import kotlin.time.Duration

sealed interface WeatherRow {
    val id: String?
    val lat: Double
    val long: Double
    val timezone: UtcOffset
    val date: LocalDate
    val hr: Int
    val temp: Measure<Temperature>
    val rh: Measure<Probability>
    val ws: Measure<Velocity>
    val prec: Measure<Length>
    val sunrise: Double?
    val sunset: Double?
    val solrad: Measure<UnitsRatio<Power, Area>>?
    val percentCured: Measure<Probability>?
    val grassFuelLoad: Measure<UnitsRatio<Mass, Area>>?

    fun clone(
        id: String? = this.id,
        lat: Double = this.lat,
        long: Double = this.long,
        timezone: UtcOffset = this.timezone,
        date: LocalDate = this.date,
        hr: Int = this.hr,
        temp: Measure<Temperature> = this.temp,
        rh: Measure<Probability> = this.rh,
        ws: Measure<Velocity> = this.ws,
        prec: Measure<Length> = this.prec,
        sunrise: Double? = this.sunrise,
        sunset: Double? = this.sunset,
        solrad: Measure<UnitsRatio<Power, Area>>? = this.solrad,
        percentCured: Measure<Probability>? = this.percentCured,
        grassFuelLoad: Measure<UnitsRatio<Mass, Area>>? = this.grassFuelLoad,
    ) = when (this) {
        is Input -> this.copy(
            id = id,
            lat = lat,
            long = long,
            timezone = timezone,
            date = date,
            hr = hr,
            temp = temp,
            rh = rh,
            ws = ws,
            prec = prec,
            sunrise = sunrise,
            sunset = sunset,
            solrad = solrad,
            percentCured = percentCured,
            grassFuelLoad = grassFuelLoad,
        )

        is Processed -> this.copy(
            id = id,
            lat = lat,
            long = long,
            timezone = timezone,
            date = date,
            hr = hr,
            temp = temp,
            rh = rh,
            ws = ws,
            prec = prec,
            sunrise = sunrise,
            sunset = sunset,
            solrad = solrad,
            percentCured = percentCured,
            grassFuelLoad = grassFuelLoad,
            mcffmc = mcffmc,
            ffmc = ffmc,
            dmc = dmc,
            dc = dc,
            isi = isi,
            bui = bui,
            fwi = fwi,
            dsr = dsr,
            mcgfmcMatted = mcgfmcMatted,
            mcgfmcStanding = mcgfmcStanding,
            gfmc = gfmc,
            gsi = gsi,
            gfwi = gfwi,
            precCumulative = precCumulative,
            canopyDrying = canopyDrying,
        )
    }

    data class Input(
        override val id: String? = null,
        override val date: LocalDate,
        override val hr: Int,
        override val temp: Measure<Temperature>,
        override val rh: Measure<Probability>,
        override val ws: Measure<Velocity>,
        override val prec: Measure<Length>,
        override val lat: Double,
        override val long: Double,
        override val timezone: UtcOffset,
        override val sunrise: Double? = null,
        override val sunset: Double? = null,
        override val solrad: Measure<UnitsRatio<Power, Area>>? = null,
        override val percentCured: Measure<Probability>? = null,
        override val grassFuelLoad: Measure<UnitsRatio<Mass, Area>>? = null,
    ) : WeatherRow

    data class Processed(
        override val id: String? = null,
        override val date: LocalDate,
        override val hr: Int,
        override val temp: Measure<Temperature>,
        override val rh: Measure<Probability>,
        override val ws: Measure<Velocity>,
        override val prec: Measure<Length>,
        override val lat: Double,
        override val long: Double,
        override val timezone: UtcOffset,
        override val sunrise: Double? = null,
        override val sunset: Double? = null,
        override val solrad: Measure<UnitsRatio<Power, Area>>? = null,
        override val percentCured: Measure<Probability>? = null,
        override val grassFuelLoad: Measure<UnitsRatio<Mass, Area>>? = null,

        val mcffmc: Measure<Probability>,
        override val ffmc: Double,
        override val dmc: Double,
        override val dc: Double,
        override val isi: Double,
        override val bui: Double,
        override val fwi: Double,
        override val dsr: Double,
        val mcgfmcMatted: Measure<Probability>,
        val mcgfmcStanding: Measure<Probability>,
        override val gfmc: Double,
        override val gsi: Double,
        override val gfwi: Double,
        val precCumulative: Measure<Length>,
        val canopyDrying: Duration
    ) : WeatherRow, IndiceComponents
}