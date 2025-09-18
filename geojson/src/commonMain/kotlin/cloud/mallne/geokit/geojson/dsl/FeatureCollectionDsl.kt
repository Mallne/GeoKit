@file:JvmName("-FeatureCollectionDslKt")

package cloud.mallne.geokit.geojson.dsl

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.Feature
import cloud.mallne.geokit.geojson.FeatureCollection
import cloud.mallne.geokit.geojson.Geometry
import kotlin.jvm.JvmName

@GeoJsonDsl
class FeatureCollectionDsl(
    private val features: MutableList<Feature> = mutableListOf(),
    var bbox: BoundingBox? = null
) {
    operator fun Feature.unaryPlus() {
        features.add(this)
    }

    fun create(): FeatureCollection =
        FeatureCollection(features, bbox)

    fun feature(
        geometry: Geometry? = null,
        id: String? = null,
        bbox: BoundingBox? = null,
        properties: PropertiesBuilder.() -> Unit = {}
    ) {
        +Feature(geometry, PropertiesBuilder().apply(properties).build(), id, bbox)
    }
}

@GeoJsonDsl
inline fun featureCollection(block: FeatureCollectionDsl.() -> Unit): FeatureCollection = FeatureCollectionDsl()
    .apply(block).create()
