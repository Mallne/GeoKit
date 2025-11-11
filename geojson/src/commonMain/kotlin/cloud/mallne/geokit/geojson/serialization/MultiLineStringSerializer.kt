package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.MultiLineString
import cloud.mallne.geokit.geojson.Position
import kotlinx.serialization.builtins.ListSerializer

internal object MultiLineStringSerializer :
    BaseGeometrySerializer<MultiLineString, List<List<Position>>>(
        "MultiLineString",
        ListSerializer(ListSerializer(Position.serializer())),
    ) {
    override fun getCoordinates(value: MultiLineString) = value.coordinates

    override fun construct(coordinates: List<List<Position>>, bbox: BoundingBox?) =
        MultiLineString(coordinates, bbox)
}
