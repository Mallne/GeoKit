@file:JvmName("-FeatureDslKt")
@file:Suppress("MatchingDeclarationName")

package cloud.mallne.geokit.geojson.dsl

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.Feature
import cloud.mallne.geokit.geojson.Geometry
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.jvm.JvmName

@GeoJsonDsl
class PropertiesBuilder {
    private val properties = mutableMapOf<String, JsonElement>()

    fun put(key: String, value: String?) {
        properties[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: Number?) {
        properties[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: Boolean?) {
        properties[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: JsonElement) {
        properties[key] = value
    }

    fun build(): Map<String, JsonElement> = properties
}

@GeoJsonDsl
inline fun feature(
    geometry: Geometry? = null,
    id: String? = null,
    bbox: BoundingBox? = null,
    properties: PropertiesBuilder.() -> Unit = {}
): Feature = Feature(geometry, PropertiesBuilder().apply(properties).build(), id, bbox)
