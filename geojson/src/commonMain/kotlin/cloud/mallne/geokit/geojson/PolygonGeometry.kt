package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.geojson.serialization.PolygonGeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.intellij.lang.annotations.Language
import kotlin.jvm.JvmStatic

/**
 * A [Geometry] that contains a single or multiple surfaces, i.e. a union type for [Polygon] and
 * [MultiPolygon].
 */
@Serializable(with = PolygonGeometrySerializer::class)
sealed interface PolygonGeometry : Geometry {
    /** Factory methods for creating and serializing [PolygonGeometry] objects. */
    companion object {
        /**
         * Decodes a JSON string into a [PolygonGeometry].
         *
         * @param json The JSON string to decode.
         * @return The decoded [PolygonGeometry].
         * @throws SerializationException if the JSON string is invalid or cannot be deserialized.
         * @throws IllegalArgumentException if the geometry does not meet structural requirements.
         */
        @JvmStatic
        fun fromJson(@Language("json") json: String): PolygonGeometry =
            GeoJson.decodeFromString(json)

        /**
         * Decodes a JSON string into a [PolygonGeometry], or returns null if deserialization fails.
         *
         * @param json The JSON string to decode.
         * @return The decoded [PolygonGeometry], or null if the string could not be deserialized.
         */
        @JvmStatic
        fun fromJsonOrNull(@Language("json") json: String): PolygonGeometry? =
            GeoJson.decodeFromStringOrNull(json)

        /**
         * Encodes a [PolygonGeometry] into a JSON string.
         *
         * @param polygonGeometry The object to encode.
         * @return The encoded JSON string.
         */
        @JvmStatic
        fun toJson(polygonGeometry: PolygonGeometry): String = polygonGeometry.toJson()
    }
}
