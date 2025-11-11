package cloud.mallne.geokit.geojson.dsl

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.MultiPolygon
import cloud.mallne.geokit.geojson.Polygon
import cloud.mallne.geokit.geojson.Position

/**
 * Builder for constructing [MultiPolygon] objects using a DSL.
 *
 * @property bbox An optional [BoundingBox] for this [MultiPolygon].
 * @see MultiPolygon
 * @see buildMultiPolygon
 * @see addPolygon
 */
@GeoJsonDsl
class MultiPolygonBuilder {
    var bbox: BoundingBox? = null
    private val coordinates: MutableList<List<List<Position>>> = mutableListOf()

    /**
     * Adds a [Polygon] to this [MultiPolygon].
     *
     * @param polygon The [Polygon] to add.
     */
    fun add(polygon: Polygon) {
        coordinates.add(polygon.coordinates)
    }

    /**
     * Builds the [MultiPolygon] from the configured values.
     *
     * @return The constructed [MultiPolygon].
     */
    fun build(): MultiPolygon = MultiPolygon(coordinates, bbox)
}
