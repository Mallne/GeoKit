package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.FeatureCollection
import cloud.mallne.geokit.geojson.serialization.BoundingBoxSerializer.toJsonArray
import cloud.mallne.geokit.geojson.serialization.FeatureSerializer.toJsonObject
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.*

object FeatureCollectionSerializer : JsonSerializer<FeatureCollection> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("FeatureCollection")

    override fun deserialize(input: JsonDecoder): FeatureCollection {
        return FeatureCollection.fromJson(input.decodeJsonElement().jsonObject)
    }

    override fun serialize(output: JsonEncoder, value: FeatureCollection) {
        val data = buildJsonObject {
            put("type", "FeatureCollection")
            value.bbox?.let { put("bbox", it.toJsonArray()) }
            put(
                "features",
                buildJsonArray {
                    value.features.forEach { add(it.toJsonObject()) }
                }
            )
        }
        output.encodeJsonElement(data)
    }
}
