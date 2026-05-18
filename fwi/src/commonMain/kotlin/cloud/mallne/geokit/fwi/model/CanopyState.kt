package cloud.mallne.geokit.fwi.model

import cloud.mallne.units.Length
import cloud.mallne.units.Measure
import cloud.mallne.units.Time
import kotlin.time.Duration

/**
 * Internal state tracker for canopy moisture.
 */
data class CanopyState(
    val rainTotalPrev: Measure<Length>,
    val dryingSinceIntercept: Duration
)