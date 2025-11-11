package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.LineString
import cloud.mallne.geokit.geojson.Position
import kotlinx.serialization.builtins.ListSerializer

internal object LineStringSerializer :
    BaseGeometrySerializer<LineString, List<Position>>(
        "LineString",
        ListSerializer(Position.serializer()),
    ) {
    override fun getCoordinates(value: LineString) = value.coordinates

    override fun construct(coordinates: List<Position>, bbox: BoundingBox?) =
        LineString(coordinates, bbox)
}
