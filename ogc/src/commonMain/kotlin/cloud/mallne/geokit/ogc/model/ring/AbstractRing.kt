package cloud.mallne.geokit.ogc.model.ring

import kotlinx.serialization.Serializable

@Serializable
sealed class AbstractRing {

    companion object {
        /**
         * Extracts all flattened coordinate pairs from a ring, handling both simple LinearRing (posList)
         * and complex Ring (CurveMember -> Curve -> Segments).
         */
        fun AbstractRing.extractCoordinates(): List<Double> {
            return when (this) {
                is LinearRing -> this.posList // Simple case
                is Ring -> {
                    // Complex case: Flatten coordinates from all segments within all curves.
                    this.curveMember.flatMap { curveMember ->
                        curveMember.curve.segments.segments.flatMap { segment ->
                            // Accumulate all posLists from all segments
                            segment.posList
                        }
                    }
                }
            }
        }
    }
}