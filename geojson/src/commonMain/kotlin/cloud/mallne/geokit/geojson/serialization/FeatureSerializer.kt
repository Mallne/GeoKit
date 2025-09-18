package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.Feature
import cloud.mallne.geokit.geojson.serialization.BoundingBoxSerializer.toJsonArray
import cloud.mallne.geokit.geojson.serialization.GeometrySerializer.toJsonObject
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.*

object FeatureSerializer : JsonSerializer<Feature> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Feature")

    override fun deserialize(input: JsonDecoder): Feature =
        Feature.fromJson(input.decodeJsonElement().jsonObject)

    override fun serialize(output: JsonEncoder, value: Feature) {
        output.encodeJsonElement(value.toJsonObject())
    }

    internal fun Feature.toJsonObject() = buildJsonObject {
        put("type", "Feature")
        bbox?.let { put("bbox", it.toJsonArray()) }
        geometry?.let { put("geometry", it.toJsonObject()) }
        id?.let { put("id", it) }

        put("properties", JsonObject(properties))
    }
}
