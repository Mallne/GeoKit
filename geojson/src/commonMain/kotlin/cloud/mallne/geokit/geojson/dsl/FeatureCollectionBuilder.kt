package cloud.mallne.geokit.geojson.dsl

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.Feature
import cloud.mallne.geokit.geojson.FeatureCollection
import cloud.mallne.geokit.geojson.Geometry
import kotlinx.serialization.Serializable

/**
 * Builder for constructing [FeatureCollection] objects using a DSL.
 *
 * @property bbox An optional [BoundingBox] for this [FeatureCollection].
 * @see FeatureCollection
 * @see buildFeatureCollection
 * @see addFeature
 */
@GeoJsonDsl
class FeatureCollectionBuilder<G : Geometry?, P : @Serializable Any?> {
    var bbox: BoundingBox? = null
    private val features: MutableList<Feature<G, P>> = mutableListOf()

    /**
     * Adds a [Feature] to this [FeatureCollection].
     *
     * @param feature The [Feature] to add.
     */
    fun add(feature: Feature<G, P>) {
        features.add(feature)
    }

    /**
     * Builds the [FeatureCollection] from the configured values.
     *
     * @return The constructed [FeatureCollection].
     */
    fun build(): FeatureCollection<G, P> = FeatureCollection(features, bbox)
}
