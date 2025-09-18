package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.jvm.JvmStatic

@Serializable(with = GeometrySerializer::class)
sealed class Geometry protected constructor() : GeoJson {
    abstract override val bbox: BoundingBox?

    override fun toString(): String = json()

    companion object {
        @JvmStatic
        fun fromJson(json: String): Geometry = fromJson(Json.decodeFromString(JsonObject.serializer(), json))

        @JvmStatic
        fun fromJsonOrNull(json: String): Geometry? = try {
            fromJson(json)
        } catch (_: Exception) {
            null
        }

        @JvmStatic
        fun fromJson(json: JsonObject): Geometry =
            when (val type = json.getValue("type").jsonPrimitive.content) {
                "Point" -> Point.fromJson(json)
                "MultiPoint" -> MultiPoint.fromJson(json)
                "LineString" -> LineString.fromJson(json)
                "MultiLineString" -> MultiLineString.fromJson(json)
                "Polygon" -> Polygon.fromJson(json)
                "MultiPolygon" -> MultiPolygon.fromJson(json)
                "GeometryCollection" -> GeometryCollection.fromJson(json)
                else -> throw IllegalArgumentException("Unsupported Geometry type \"$type\"")
            }
    }
}



