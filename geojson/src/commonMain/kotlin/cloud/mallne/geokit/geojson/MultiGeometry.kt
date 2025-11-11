package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.geojson.serialization.MultiGeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.intellij.lang.annotations.Language
import kotlin.jvm.JvmStatic

/**
 * A [Geometry] that contains multiple homogenous points, curves, or surfaces, i.e. a union type for
 * [MultiPoint], [MultiLineString], and [MultiPolygon].
 */
@Serializable(with = MultiGeometrySerializer::class)
sealed interface MultiGeometry : Geometry {
    /** Factory methods for creating and serializing [MultiGeometry] objects. */
    companion object {
        /**
         * Decodes a JSON string into a [MultiGeometry].
         *
         * @param json The JSON string to decode.
         * @return The decoded [MultiGeometry].
         * @throws SerializationException if the JSON string is invalid or cannot be deserialized.
         * @throws IllegalArgumentException if the geometry does not meet structural requirements.
         */
        @JvmStatic
        fun fromJson(@Language("json") json: String): MultiGeometry =
            GeoJson.decodeFromString(json)

        /**
         * Decodes a JSON string into a [MultiGeometry], or returns null if deserialization fails.
         *
         * @param json The JSON string to decode.
         * @return The decoded [MultiGeometry], or null if the string could not be deserialized.
         */
        @JvmStatic
        fun fromJsonOrNull(@Language("json") json: String): MultiGeometry? =
            GeoJson.decodeFromStringOrNull(json)

        /**
         * Encodes a [MultiGeometry] into a JSON string.
         *
         * @param multiGeometry The object to encode.
         * @return The encoded JSON string.
         */
        @JvmStatic
        fun toJson(multiGeometry: MultiGeometry): String = multiGeometry.toJson()
    }
}
