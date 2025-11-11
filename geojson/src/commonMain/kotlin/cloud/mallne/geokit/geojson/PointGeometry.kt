package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.geojson.serialization.PointGeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.intellij.lang.annotations.Language
import kotlin.jvm.JvmStatic

/**
 * A [Geometry] that contains a single or multiple points, i.e. a union type for [Point] and
 * [MultiPoint].
 */
@Serializable(with = PointGeometrySerializer::class)
sealed interface PointGeometry : Geometry {
    /** Factory methods for creating and serializing [PointGeometry] objects. */
    companion object {
        /**
         * Decodes a JSON string into a [PointGeometry].
         *
         * @param json The JSON string to decode.
         * @return The decoded [PointGeometry].
         * @throws SerializationException if the JSON string is invalid or cannot be deserialized.
         * @throws IllegalArgumentException if the geometry does not meet structural requirements.
         */
        @JvmStatic
        fun fromJson(@Language("json") json: String): PointGeometry =
            GeoJson.decodeFromString(json)

        /**
         * Decodes a JSON string into a [PointGeometry], or returns null if deserialization fails.
         *
         * @param json The JSON string to decode.
         * @return The decoded [PointGeometry], or null if the string could not be deserialized.
         */
        @JvmStatic
        fun fromJsonOrNull(@Language("json") json: String): PointGeometry? =
            GeoJson.decodeFromStringOrNull(json)

        /**
         * Encodes a [PointGeometry] into a JSON string.
         *
         * @param pointGeometry The object to encode.
         * @return The encoded JSON string.
         */
        @JvmStatic
        fun toJson(pointGeometry: PointGeometry): String = pointGeometry.toJson()
    }
}
