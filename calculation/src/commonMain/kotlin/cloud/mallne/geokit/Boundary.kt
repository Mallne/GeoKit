package cloud.mallne.geokit

import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
data class Boundary(
    val southWest: Vertex,
    val northEast: Vertex,
) {
    val north
        get() = northEast.latitude
    val east
        get() = northEast.longitude
    val south
        get() = southWest.latitude
    val west
        get() = southWest.longitude

    val southEast: Vertex
        get() {
            return Vertex(south, east)
        }
    val northWest: Vertex
        get() {
            return Vertex(north, west)
        }

    val center: Vertex
        get() = GeokitMeasurement.center(this)

    val spanLatitude: Double
        get() = abs(north - south)

    val spanLongitude: Double
        get() = abs(east - west)

    constructor(north: Double, east: Double, south: Double, west: Double) : this(
        southWest = Vertex(south, west),
        northEast = Vertex(north, east)
    )

    private fun containsLatitude(latitude: Double): Boolean {
        return latitude in south..north
    }

    private fun containsLongitude(longitude: Double): Boolean {
        return longitude in west..east
    }

    /**
     * Determines whether this LatLngBounds contains a point.
     */
    operator fun contains(latLng: Vertex): Boolean {
        return (containsLatitude(latLng.latitude) && containsLongitude(latLng.longitude))
    }

    init {
        require(!(north.isNaN() || south.isNaN())) { "latitude must not be NaN" }
        require(!(east.isNaN() || west.isNaN())) { "longitude must not be NaN" }
        require(!(east.isInfinite() || west.isInfinite())) { "longitude must not be infinite" }
        require(
            !(north > 90 || north < -90 || south > 90 || south < -90)
        ) { "latitude must be between -90 and 90" }
        require(north >= south) { "latNorth cannot be less than latSouth" }
        require(east >= west) { "lonEast cannot be less than lonWest" }
    }
}
