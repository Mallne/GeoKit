package cloud.mallne.geokit.fwi.calculator.indices

import cloud.mallne.geokit.fwi.calculator.Util.getSunlight
import cloud.mallne.geokit.fwi.calculator.Util.rainSinceInterceptReset
import cloud.mallne.geokit.fwi.calculator.Util.seasonalCuring
import cloud.mallne.geokit.fwi.calculator.indices.FineFuelMoistureContent.FFMC_DEFAULT
import cloud.mallne.geokit.fwi.calculator.indices.FineFuelMoistureContent.FFMC_INTERCEPT
import cloud.mallne.geokit.fwi.calculator.indices.FineFuelMoistureContent.ffmcToMcffmc
import cloud.mallne.geokit.fwi.calculator.indices.FineFuelMoistureContent.mcffmcToFfmc
import cloud.mallne.geokit.fwi.calculator.indices.GrasslandFuelMoistureContent.DEFAULT_GRASS_FUEL_LOAD
import cloud.mallne.geokit.fwi.model.CanopyState
import cloud.mallne.geokit.fwi.model.WeatherRow
import co.touchlab.kermit.Logger
import kotlinx.datetime.LocalDate
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow

object FireWeatherIndex {
    internal const val GRASS_TRANSITION = true
    internal const val MON_STANDING = 7
    internal const val DAY_STANDING = 1
    internal const val CONTINUOUS_MULTIYEAR = false

    /**
     * Calculate Fire Weather Index (FWI)
     *
     * @param isi [Double]            Initial Spread Index
     * @param bui [Double]            Build-up Index
     * @return    [Double]            Fire Weather Index
     */
    operator fun invoke(isi: Double, bui: Double): Double {
        val bb = if (bui > 80.0) {
            0.1 * isi * 1000.0 / (25.0 + 108.64 / exp(0.023 * bui))
        } else {
            0.1 * isi * (0.626 * bui.pow(0.809) + 2.0)
        }
        return if (bb <= 1.0) bb else exp(2.72 * (0.434 * ln(bb)).pow(0.647))
    }

    internal fun dailySeverityRating(fwi: Double): Double = 0.0272 * fwi.pow(1.77)

    /**
     * Calculate hourly FWI indices from hourly weather stream for a single station.
     */
    private fun stnHFWI(
        w: List<WeatherRow>,
        ffmcOld: Double?,
        mcffmcOld: Double?,
        dmcOld: Double,
        dcOld: Double,
        mcgfmcMattedOld: Double,
        mcgfmcStandingOld: Double,
        precCumulative: Double,
        canopyDrying: Double
    ): List<WeatherRow.Processed> {

        // Initial Moisture setup (XOR logic for FFMC/MCFFMC)
        var mcffmc = when {
            mcffmcOld != null -> mcffmcOld
            ffmcOld != null -> ffmcToMcffmc(ffmcOld)
            else -> throw IllegalArgumentException("Either ffmcOld or mcffmcOld must be provided")
        }

        var mcgfmcMatted = mcgfmcMattedOld
        var mcgfmcStanding = mcgfmcStandingOld
        var mcdmc = DuffMoistureCode.dmcToMcdmc(dmcOld)
        var mcdc = DroughtCode.dcToMcdc(dcOld)

        var currentCanopy = CanopyState(
            rainTotalPrev = precCumulative,
            dryingSinceIntercept = canopyDrying
        )

        // Grass transition date logic
        val firstRow = w.first()
        var dateGrassStanding = LocalDate(firstRow.date.year, MON_STANDING, DAY_STANDING)
        if (dateGrassStanding < firstRow.date) {
            dateGrassStanding = LocalDate(firstRow.date.year + 1, MON_STANDING, DAY_STANDING)
        }

        return w.map { cur ->
            // 1. Canopy & Rain Interception
            currentCanopy = rainSinceInterceptReset(cur.prec, currentCanopy)

            val rainFfmc = when {
                currentCanopy.rainTotalPrev + cur.prec <= FFMC_INTERCEPT -> 0.0
                currentCanopy.rainTotalPrev > FFMC_INTERCEPT -> cur.prec
                else -> currentCanopy.rainTotalPrev + cur.prec - FFMC_INTERCEPT
            }

            // 2. FFMC Calculation
            mcffmc = FineFuelMoistureContent(mcffmc, cur.temp, cur.rh, cur.ws, rainFfmc)
            val ffmc = mcffmcToFfmc(mcffmc)

            // 3. DMC & DC (Duff and Drought)
            mcdmc = DuffMoistureCode(
                mcdmc, cur.hr.toDouble(), cur.temp, cur.rh, cur.prec,
                cur.sunrise ?: 0.0, cur.sunset ?: 0.0, currentCanopy.rainTotalPrev
            )
            val dmc = DuffMoistureCode.mcdmcToDmc(mcdmc)

            mcdc = DroughtCode(
                mcdc, cur.hr.toDouble(), cur.temp, cur.prec,
                cur.sunrise ?: 0.0, cur.sunset ?: 0.0, currentCanopy.rainTotalPrev
            )
            val dc = DroughtCode.mcdcToDc(mcdc)

            // 4. Spread & Fire Indices
            val isi = InitialSpreadIndex(cur.ws, ffmc)
            val bui = BuildUpIndex(dmc, dc)
            val fwi = FireWeatherIndex(isi, bui)
            val dsr = dailySeverityRating(fwi)

            // Update canopy for next iteration
            currentCanopy = currentCanopy.copy(
                rainTotalPrev = currentCanopy.rainTotalPrev + cur.prec
            )

            // 5. Grassland Moisture
            mcgfmcMatted = GrasslandFuelMoistureContent(
                mcgfmcMatted, cur.temp, cur.rh, cur.ws, cur.prec,
                cur.solrad ?: 0.0, cur.grassFuelLoad ?: DEFAULT_GRASS_FUEL_LOAD
            )

            // Standing assumes no solar and reduced rain absorption (6% effectiveness)
            mcgfmcStanding = GrasslandFuelMoistureContent(
                mcgfmcStanding, cur.temp, cur.rh, cur.ws, cur.prec * 0.06,
                0.0, cur.grassFuelLoad ?: DEFAULT_GRASS_FUEL_LOAD
            )

            val isStanding = cur.date >= dateGrassStanding
            val mcgfmc = if (isStanding) mcgfmcStanding else mcgfmcMatted

            val gfmc = GrasslandFuelMoistureContent.mcgfmcToGfmc(mcgfmc, cur.percentCured ?: 100.0, cur.ws)
            val gsi = GrasslandSpreadIndex(cur.ws, mcgfmc, cur.percentCured ?: 100.0, isStanding)
            val gfwi = GrasslandFireWeatherIndex(gsi, cur.grassFuelLoad ?: DEFAULT_GRASS_FUEL_LOAD)

            // Construct Processed Row
            WeatherRow.Processed(
                id = cur.id,
                date = cur.date,
                hr = cur.hr,
                temp = cur.temp,
                rh = cur.rh,
                ws = cur.ws,
                prec = cur.prec,
                lat = cur.lat,
                long = cur.long,
                timezone = cur.timezone,
                sunrise = cur.sunrise,
                sunset = cur.sunset,
                solrad = cur.solrad,
                percentCured = cur.percentCured,
                grassFuelLoad = cur.grassFuelLoad,
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
                precCumulative = currentCanopy.rainTotalPrev,
                canopyDrying = currentCanopy.dryingSinceIntercept
            )
        }
    }

    /**
     * High-level orchestrator for Hourly Fire Weather Index calculations.
     *
     * @param    dfWx               hourly values weather stream
     * @param    ffmcOld            previous value for FFMC (startup 85, None for mcffmc_old)
     * @param    mcffmcOld          previous value mcffmc (default None for ffmc_old input)
     * @param    dmcOld             previous value for DMC (startup 6)
     * @param    dcOld              previous value for DC (startup 15)
     * @param    mcgfmcMattedOld   previous value for matted mcgfmc (startup FFMC = 85)
     * @param    mcgfmcStandingOld previous value for standing mcgfmc (startup FFMC = 85)
     * @param    precCumulative     cumulative precipitation this rainfall (default 0)
     * @param    canopyDrying       consecutive hours of no rain (default 0)
     * @param    silent              suppresses informative print statements (default False)
     * @return                       hourly values FWI and weather stream
     */
    operator fun invoke(
        dfWx: List<WeatherRow>,
        timezone: Double? = null,
        ffmcOld: Double? = FFMC_DEFAULT,
        mcffmcOld: Double? = null,
        dmcOld: Double = DuffMoistureCode.DMC_DEFAULT,
        dcOld: Double = DroughtCode.DC_DEFAULT,
        mcgfmcMattedOld: Double = ffmcToMcffmc(FFMC_DEFAULT),
        mcgfmcStandingOld: Double = ffmcToMcffmc(FFMC_DEFAULT),
        precCumulative: Double = 0.0,
        canopyDrying: Double = 0.0,
        getSolrad: Boolean? = null,
        silent: Boolean = false
    ): List<WeatherRow.Processed> {

        if (!silent) log.i("\n########\nFWI2025 (KMP-Precision)\n")

        // 1. Column Check & Enrichment (mirroring hFWI wrapper)
        val needsSolrad = getSolrad ?: dfWx.any { it.solrad == null }

        val enrichedData = dfWx.map { row ->
            val tz = timezone ?: row.timezone
            var updated = row.clone(timezone = tz)

            if (updated.percentCured == null) {
                updated = updated.clone(percentCured = seasonalCuring(updated.date))
            }
            if (updated.grassFuelLoad == null) {
                updated = updated.clone(grassFuelLoad = 0.35) // DEFAULT_GRASS_FUEL_LOAD
            }
            updated
        }

        if (!silent) {
            log.i("Startup values used:")
            if (ffmcOld == null) {
                log.i("FFMC = None and mcffmc = $mcffmcOld %")
            } else if (mcffmcOld == null) {
                log.i("FFMC = $ffmcOld % and mcffmc = None")
            }
            log.i("DMC = $dmcOld and DC = $dcOld")
            log.i("mcgfmc matted = $mcgfmcMattedOld % and standing = $mcgfmcStandingOld %")
            log.i("cumulative precipitation = $precCumulative mm and canopy drying = $canopyDrying\n")
        }

        // 2. Grouping & Execution
        val groups = if (CONTINUOUS_MULTIYEAR) {
            enrichedData.groupBy { it.id }
        } else {
            enrichedData.groupBy { it.id to it.date.year }
        }

        return groups.flatMap { (key, stationStream) ->
            if (!silent) {
                if (CONTINUOUS_MULTIYEAR) {
                    log.d("Running station $key")
                } else {
                    val pair = key as Pair<*, *>
                    log.d("Running ${pair.first} for ${pair.second}")
                }
            }

            // 3. Sequential Sorting & Solar Calculation (matching Python's loop order)
            val sorted = stationStream.sortedWith(compareBy<WeatherRow> { it.date }.thenBy { it.hr })
            val withSolar = getSunlight(sorted, getSolrad = needsSolrad)

            stnHFWI(
                w = withSolar,
                ffmcOld = ffmcOld,
                mcffmcOld = mcffmcOld,
                dmcOld = dmcOld,
                dcOld = dcOld,
                mcgfmcMattedOld = mcgfmcMattedOld,
                mcgfmcStandingOld = mcgfmcStandingOld,
                precCumulative = precCumulative,
                canopyDrying = canopyDrying
            )
        }
    }

    private val log = Logger.withTag("FireWeatherIndex")
}