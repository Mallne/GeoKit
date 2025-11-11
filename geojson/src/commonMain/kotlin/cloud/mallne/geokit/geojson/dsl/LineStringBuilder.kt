package cloud.mallne.geokit.geojson.dsl

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.LineString
import cloud.mallne.geokit.geojson.Point
import cloud.mallne.geokit.geojson.Position

/**
 * Builder for constructing [LineString] objects using a DSL.
 *
 * @property bbox An optional [BoundingBox] for this [LineString].
 * @see LineString
 * @see buildLineString
 */
@GeoJsonDsl
class LineStringBuilder {
    var bbox: BoundingBox? = null
    private val points: MutableList<Position> = mutableListOf()

    /**
     * Adds a [Position] to this [LineString].
     *
     * @param longitude The longitude coordinate.
     * @param latitude The latitude coordinate.
     * @param altitude The optional altitude coordinate.
     */
    fun add(longitude: Double, latitude: Double, altitude: Double? = null) {
        points.add(Position(longitude, latitude, altitude))
    }

    /**
     * Adds a [Position] to this [LineString].
     *
     * @param position The [Position] to add.
     */
    fun add(position: Position) {
        points.add(position)
    }

    /**
     * Adds a [Point] to this [LineString].
     *
     * @param point The [Point] whose coordinates will be added.
     */
    fun add(point: Point) {
        points.add(point.coordinates)
    }

    /**
     * Builds the [LineString] from the configured values.
     *
     * @return The constructed [LineString].
     */
    fun build(): LineString = LineString(points, bbox)
}
