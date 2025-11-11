package cloud.mallne.geokit.geojson

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import org.intellij.lang.annotations.Language

/**
 * Main entry point for encoding and decoding [GeoJsonObject] objects.
 *
 * This object provides serialization and deserialization functionality for all GeoJSON types using
 * kotlinx.serialization.
 */
data object GeoJson {
    internal const val STRICT: Boolean = false

    /**
     * The default Json configuration for [GeoJsonObject] objects.
     *
     * This configuration ignores unknown keys and includes polymorphic serializers for all
     * [GeoJsonObject] types.
     */
    val jsonFormat: Json = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            polymorphicDefaultSerializer(GeoJsonObject::class) { obj ->
                val serializer =
                    when (obj) {
                        is Point -> Point.Companion.serializer()
                        is MultiPoint -> MultiPoint.Companion.serializer()
                        is LineString -> LineString.Companion.serializer()
                        is MultiLineString -> MultiLineString.Companion.serializer()
                        is Polygon -> Polygon.Companion.serializer()
                        is MultiPolygon -> MultiPolygon.Companion.serializer()
                        is GeometryCollection<*> ->
                            GeometryCollection.Companion.serializer(Geometry.Companion.serializer())

                        is Feature<*, *> ->
                            Feature.serializer(
                                Geometry.Companion.serializer().nullable,
                                when (val props = obj.properties) {
                                    is JsonObject? -> JsonObject.serializer().nullable
                                    else -> {
                                        @OptIn(InternalSerializationApi::class)
                                        props::class.serializer()
                                    }
                                },
                            )

                        is FeatureCollection<*, *> ->
                            FeatureCollection.serializer(
                                Geometry.Companion.serializer().nullable,
                                when (
                                    val props = obj.features.firstNotNullOfOrNull { it.properties }
                                ) {
                                    is JsonObject? -> JsonObject.serializer().nullable
                                    else -> {
                                        @OptIn(InternalSerializationApi::class)
                                        props::class.serializer()
                                    }
                                },
                            )
                    }
                @Suppress("UNCHECKED_CAST")
                serializer as SerializationStrategy<GeoJsonObject>
            }
        }
    }

    @PublishedApi
    internal inline fun <reified T : GeoJsonObject?> encodeToString(value: T): String =
        jsonFormat.encodeToString<T>(value)

    @PublishedApi
    internal inline fun <reified T : GeoJsonObject?> decodeFromString(
        @Language("json") string: String
    ): T = jsonFormat.decodeFromString<T>(string)

    @PublishedApi
    internal inline fun <reified T : GeoJsonObject?> decodeFromStringOrNull(
        @Language("json") string: String
    ): T? =
        try {
            decodeFromString<T>(string)
        } catch (_: IllegalArgumentException) {
            null
        }
}
