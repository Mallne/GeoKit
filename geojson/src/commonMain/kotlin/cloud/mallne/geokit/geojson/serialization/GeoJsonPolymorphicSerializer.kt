package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.*
import kotlin.reflect.KClass

internal abstract class GeoJsonPolymorphicSerializer<T : GeoJsonObject>(
    baseClass: KClass<T>,
    private val allowedTypes: Set<String>,
) : JsonContentPolymorphicSerializer<T>(baseClass) {

    private val allowedSerializers by lazy { allSerializers.filter { it.key in allowedTypes } }

    @OptIn(ExperimentalSerializationApi::class)
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<T> {
        element as? JsonObject ?: throw SerializationException("Expected JSON object")
        val type =
            element["type"]?.let { Json.decodeFromJsonElement<String>(it.jsonPrimitive) }
                ?: throw MissingFieldException("type", "GeoJsonObject")
        val actualSerializer =
            allowedSerializers[type]
                ?: throw SerializationException(
                    "Unexpected type $type; expected one of: ${allowedTypes.joinToString()}"
                )
        @Suppress("UNCHECKED_CAST")
        return actualSerializer as DeserializationStrategy<T>
    }

    internal companion object {
        val allSerializers by lazy {
            mapOf(
                "Point" to Point.serializer(),
                "MultiPoint" to MultiPoint.serializer(),
                "LineString" to LineString.serializer(),
                "MultiLineString" to MultiLineString.serializer(),
                "Polygon" to Polygon.serializer(),
                "MultiPolygon" to MultiPolygon.serializer(),
                "GeometryCollection" to GeometryCollection.serializer(Geometry.serializer()),
                "Feature" to
                        Feature.serializer(
                            Geometry.serializer().nullable,
                            JsonObject.serializer().nullable,
                        ),
                "FeatureCollection" to
                        FeatureCollection.serializer(
                            Geometry.serializer().nullable,
                            JsonObject.serializer().nullable,
                        ),
            )
        }
    }
}
