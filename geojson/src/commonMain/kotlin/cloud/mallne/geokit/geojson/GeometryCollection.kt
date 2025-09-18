package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.geojson.serialization.GeometrySerializer
import cloud.mallne.geokit.geojson.serialization.jsonJoin
import cloud.mallne.geokit.geojson.serialization.jsonProp
import cloud.mallne.geokit.geojson.serialization.toBbox
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable(with = GeometrySerializer::class)
class GeometryCollection @JvmOverloads constructor(
    val geometries: List<Geometry>,
    override val bbox: BoundingBox? = null
) : Geometry(), Collection<Geometry> by geometries {
    @JvmOverloads
    constructor(vararg geometries: Geometry, bbox: BoundingBox? = null) : this(geometries.toList(), bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GeometryCollection

        if (geometries != other.geometries) return false
        if (bbox != other.bbox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = geometries.hashCode()
        result = 31 * result + (bbox?.hashCode() ?: 0)
        return result
    }

    override fun json(): String =
        """{"type":"GeometryCollection",${bbox.jsonProp()}"geometries":${geometries.jsonJoin { it.json() }}}"""

    companion object {
        @JvmStatic
        fun fromJson(json: String): GeometryCollection =
            fromJson(Json.decodeFromString(JsonObject.serializer(), json))

        @JvmStatic
        fun fromJsonOrNull(json: String): GeometryCollection? = try {
            fromJson(json)
        } catch (_: Exception) {
            null
        }

        @JvmStatic
        fun fromJson(json: JsonObject): GeometryCollection {
            require(json.getValue("type").jsonPrimitive.content == "GeometryCollection") {
                "Object \"type\" is not \"GeometryCollection\"."
            }

            val geometries = json.getValue("geometries").jsonArray.map {
                Geometry.fromJson(it.jsonObject)
            }

            val bbox = json["bbox"]?.jsonArray?.toBbox()

            return GeometryCollection(geometries, bbox)
        }
    }
}
