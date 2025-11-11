package cloud.mallne.geokit.geojson.dsl

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.Geometry
import cloud.mallne.geokit.geojson.GeometryCollection

/**
 * Builder for constructing [GeometryCollection] objects using a DSL.
 *
 * @property bbox An optional [BoundingBox] for this [GeometryCollection].
 * @see GeometryCollection
 * @see buildGeometryCollection
 * @see addPoint
 * @see addLineString
 * @see addPolygon
 * @see addMultiPoint
 * @see addMultiLineString
 * @see addMultiPolygon
 * @see addGeometryCollection
 */
@GeoJsonDsl
class GeometryCollectionBuilder<G : Geometry> {
    var bbox: BoundingBox? = null
    private val geometries: MutableList<G> = mutableListOf()

    /**
     * Adds a [Geometry] to this [GeometryCollection].
     *
     * @param geometry The [Geometry] to add.
     */
    fun add(geometry: G) {
        geometries.add(geometry)
    }

    /**
     * Builds the [GeometryCollection] from the configured values.
     *
     * @return The constructed [GeometryCollection].
     */
    fun build(): GeometryCollection<G> = GeometryCollection(geometries, bbox)
}
