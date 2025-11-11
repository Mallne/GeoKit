package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.geojson.serialization.GeoJsonObjectSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.intellij.lang.annotations.Language
import kotlin.jvm.JvmStatic

/**
 * A [GeoJsonObject] represents a [Geometry], [Feature], or [FeatureCollection].
 *
 * @property bbox An optional [BoundingBox] used to represent the limits of the object's [Geometry].
 */
@Serializable(with = GeoJsonObjectSerializer::class)
sealed interface GeoJsonObject {
    val bbox: BoundingBox?

    /** Factory methods for creating and serializing [GeoJsonObject] objects. */
    companion object {
        /**
         * Decodes a JSON string into a [GeoJsonObject].
         *
         * @param json The JSON string to decode.
         * @return The decoded [GeoJsonObject].
         * @throws SerializationException if the JSON string is invalid or cannot be deserialized.
         * @throws IllegalArgumentException if the JSON contains an invalid [GeoJsonObject].
         */
        @JvmStatic
        fun fromJson(@Language("json") json: String): GeoJsonObject =
            GeoJson.decodeFromString(json)

        /**
         * Decodes a JSON string into a [GeoJsonObject], or returns null if deserialization fails.
         *
         * @param json The JSON string to decode.
         * @return The decoded [GeoJsonObject], or null if the string could not be deserialized.
         */
        @JvmStatic
        fun fromJsonOrNull(@Language("json") json: String): GeoJsonObject? =
            GeoJson.decodeFromStringOrNull(json)

        /**
         * Encodes a [GeoJsonObject] into a JSON string.
         *
         * The restrictions described in [cloud.mallne.geokit.geojson.toJson] apply.
         *
         * @param geoJsonObject The object to encode.
         * @return The encoded JSON string.
         * @throws SerializationException if serialization fails.
         */
        @JvmStatic
        fun toJson(geoJsonObject: GeoJsonObject): String = geoJsonObject.toJson()
    }
}
