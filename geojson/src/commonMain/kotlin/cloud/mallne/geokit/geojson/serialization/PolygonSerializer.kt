package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.Polygon
import cloud.mallne.geokit.geojson.Position
import kotlinx.serialization.builtins.ListSerializer

internal object PolygonSerializer :
    BaseGeometrySerializer<Polygon, List<List<Position>>>(
        "Polygon",
        ListSerializer(ListSerializer(Position.serializer())),
    ) {
    override fun getCoordinates(value: Polygon) = value.coordinates

    override fun construct(coordinates: List<List<Position>>, bbox: BoundingBox?) =
        Polygon(coordinates, bbox)
}
