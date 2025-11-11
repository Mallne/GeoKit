package cloud.mallne.geokit.geojson.dsl

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.LineString
import cloud.mallne.geokit.geojson.MultiLineString
import cloud.mallne.geokit.geojson.Position

/**
 * Builder for constructing [MultiLineString] objects using a DSL.
 *
 * @property bbox An optional [BoundingBox] for this [MultiLineString].
 * @see MultiLineString
 * @see buildMultiLineString
 * @see addLineString
 */
@GeoJsonDsl
class MultiLineStringBuilder {
    var bbox: BoundingBox? = null
    private val coordinates: MutableList<List<Position>> = mutableListOf()

    /**
     * Adds a [LineString] to this [MultiLineString].
     *
     * @param lineString The [LineString] to add.
     */
    fun add(lineString: LineString) {
        coordinates.add(lineString.coordinates)
    }

    /**
     * Builds the [MultiLineString] from the configured values.
     *
     * @return The constructed [MultiLineString].
     */
    fun build(): MultiLineString = MultiLineString(coordinates, bbox)
}
