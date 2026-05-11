package cloud.mallne.geokit.fwi.model

/**
 * Internal state tracker for canopy moisture.
 */
data class CanopyState(
    val rainTotalPrev: Double,
    val dryingSinceIntercept: Double
)