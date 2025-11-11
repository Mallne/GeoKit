package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.geojson.serialization.FeatureIdSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

/**
 * Represents the identifier for a GeoJSON Feature. According to the GeoJSON specification, a
 * Feature may have an identifier that can be a string or number.
 *
 * When serializing to/from a non-JSON format, the identifier will be serialized as a string,
 * including quotes if it's not a number.
 */
typealias FeatureId = @Serializable(with = FeatureIdSerializer::class) JsonPrimitive
