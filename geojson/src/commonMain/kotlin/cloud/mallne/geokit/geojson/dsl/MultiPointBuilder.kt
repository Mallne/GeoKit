package cloud.mallne.geokit.geojson.dsl

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.MultiPoint
import cloud.mallne.geokit.geojson.Point
import cloud.mallne.geokit.geojson.Position

/**
 * Builder for constructing [MultiPoint] objects using a DSL.
 *
 * @property bbox An optional [BoundingBox] for this [MultiPoint].
 * @see MultiPoint
 * @see buildMultiPoint
 * @see addPoint
 */
@GeoJsonDsl
class MultiPointBuilder {
    var bbox: BoundingBox? = null
    private val points: MutableList<Position> = mutableListOf()

    /**
     * Adds a [Point] to this [MultiPoint].
     *
     * @param longitude The longitude coordinate.
     * @param latitude The latitude coordinate.
     * @param altitude The optional altitude coordinate.
     */
    fun add(longitude: Double, latitude: Double, altitude: Double? = null) {
        points.add(Position(longitude, latitude, altitude))
    }

    /**
     * Adds a [Point] to this [MultiPoint].
     *
     * @param position The [Position] to add.
     */
    fun add(position: Position) {
        points.add(position)
    }

    /**
     * Adds a [Point] to this [MultiPoint].
     *
     * @param point The [Point] whose coordinates will be added.
     */
    fun add(point: Point) {
        points.add(point.coordinates)
    }

    /**
     * Builds the [MultiPoint] from the configured values.
     *
     * @return The constructed [MultiPoint].
     */
    fun build(): MultiPoint = MultiPoint(points, bbox)
}
