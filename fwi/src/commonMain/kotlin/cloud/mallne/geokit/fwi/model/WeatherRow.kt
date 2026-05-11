package cloud.mallne.geokit.fwi.model

import kotlinx.datetime.LocalDate

sealed interface WeatherRow {
    // Shared identity and weather input properties
    val id: String?
    val lat: Double
    val long: Double
    val timezone: Double
    val date: LocalDate
    val hr: Int
    val temp: Double
    val rh: Double
    val ws: Double
    val prec: Double
    val sunrise: Double?
    val sunset: Double?
    val solrad: Double?
    val percentCured: Double?
    val grassFuelLoad: Double?

    fun clone(
        id: String? = this.id,
        lat: Double = this.lat,
        long: Double = this.long,
        timezone: Double = this.timezone,
        date: LocalDate = this.date,
        hr: Int = this.hr,
        temp: Double = this.temp,
        rh: Double = this.rh,
        ws: Double = this.ws,
        prec: Double = this.prec,
        sunrise: Double? = this.sunrise,
        sunset: Double? = this.sunset,
        solrad: Double? = this.solrad,
        percentCured: Double? = this.percentCured,
        grassFuelLoad: Double? = this.grassFuelLoad,
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

    /**
     * Represents raw input weather data before FWI processing.
     */
    data class Input(
        override val id: String? = null,
        override val date: LocalDate,
        override val hr: Int,
        override val temp: Double,
        override val rh: Double,
        override val ws: Double,
        override val prec: Double,
        override val lat: Double,
        override val long: Double,
        override val timezone: Double,
        override val sunrise: Double? = null,
        override val sunset: Double? = null,
        override val solrad: Double? = null,
        override val percentCured: Double? = null,
        override val grassFuelLoad: Double? = null,
    ) : WeatherRow

    /**
     * Represents a fully processed row containing all FWI and Grassland indices.
     */
    data class Processed(
        override val id: String? = null,
        override val date: LocalDate,
        override val hr: Int,
        override val temp: Double,
        override val rh: Double,
        override val ws: Double,
        override val prec: Double,
        override val lat: Double,
        override val long: Double,
        override val timezone: Double,
        override val sunrise: Double? = null,
        override val sunset: Double? = null,
        override val solrad: Double? = null,
        override val percentCured: Double? = null,
        override val grassFuelLoad: Double? = null,

        // Calculated Indices (Immutable in the processed state)
        val mcffmc: Double,
        val ffmc: Double,
        val dmc: Double,
        val dc: Double,
        val isi: Double,
        val bui: Double,
        val fwi: Double,
        val dsr: Double,
        val mcgfmcMatted: Double,
        val mcgfmcStanding: Double,
        val gfmc: Double,
        val gsi: Double,
        val gfwi: Double,
        val precCumulative: Double,
        val canopyDrying: Double
    ) : WeatherRow
}