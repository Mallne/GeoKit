package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.geojson.serialization.GeometryCollectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.intellij.lang.annotations.Language
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

/**
 * A [GeometryCollection] contains multiple, heterogeneous [Geometry] objects.
 *
 * See [RFC 7946 Section 3.1.8](https://tools.ietf.org/html/rfc7946#section-3.1.8) for the full
 * specification.
 */
@Serializable(with = GeometryCollectionSerializer::class)
data class GeometryCollection<out G : Geometry>
@JvmOverloads
constructor(
    /** The [Geometry] objects in this [GeometryCollection]. */
    val geometries: List<G>,
    /** The [BoundingBox] of this [GeometryCollection]. */
    override val bbox: BoundingBox? = null,
) : Geometry, Collection<G> by geometries {

    /**
     * Create a [GeometryCollection] by a number of [Geometry] objects.
     *
     * @param geometries The [Geometry] objects that make up this [GeometryCollection].
     * @param bbox The [BoundingBox] of this [GeometryCollection].
     */
    @JvmOverloads
    constructor(
        vararg geometries: G,
        bbox: BoundingBox? = null,
    ) : this(geometries.toList(), bbox)

    /** Factory methods for creating and serializing [GeometryCollection] objects. */
    companion object {
        /**
         * Deserialize a [GeometryCollection] from a JSON string.
         *
         * @param json The JSON string to parse.
         * @return The deserialized [GeometryCollection].
         * @throws SerializationException if the JSON string is invalid or cannot be deserialized.
         * @throws IllegalArgumentException if the JSON contains an invalid [GeometryCollection].
         */
        @JvmSynthetic
        @JvmName("inlineFromJson")
        inline fun <reified G : Geometry> fromJson(
            @Language("json") json: String
        ): GeometryCollection<G> = GeoJson.decodeFromString(json)

        /**
         * Deserialize a [GeometryCollection] from a JSON string, or null if parsing fails.
         *
         * @param json The JSON string to parse.
         * @return The deserialized [GeometryCollection], or null if parsing fails.
         */
        @JvmSynthetic
        @JvmName("inlineFromJsonOrNull")
        inline fun <reified T : Geometry> fromJsonOrNull(
            @Language("json") json: String
        ): GeometryCollection<T>? = GeoJson.decodeFromStringOrNull(json)

        // Publish for Java below; Kotlin should use the inline reified versions above

        @PublishedApi
        @JvmStatic
        internal fun fromJson(json: String): GeometryCollection<*> =
            GeoJson.decodeFromString<GeometryCollection<Geometry>>(json)

        @PublishedApi
        @JvmStatic
        internal fun fromJsonOrNull(json: String): GeometryCollection<*>? =
            GeoJson.decodeFromStringOrNull<GeometryCollection<Geometry>>(json)

        @PublishedApi
        @JvmStatic
        internal fun toJson(geometryCollection: GeometryCollection<Geometry>): String =
            geometryCollection.toJson()
    }
}
