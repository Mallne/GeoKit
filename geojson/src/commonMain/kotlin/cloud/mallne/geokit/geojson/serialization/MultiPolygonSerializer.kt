package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.MultiPolygon
import cloud.mallne.geokit.geojson.Position
import kotlinx.serialization.builtins.ListSerializer

internal object MultiPolygonSerializer :
    BaseGeometrySerializer<MultiPolygon, List<List<List<Position>>>>(
        "MultiPolygon",
        ListSerializer(ListSerializer(ListSerializer(Position.serializer()))),
    ) {
    override fun getCoordinates(value: MultiPolygon) = value.coordinates

    override fun construct(coordinates: List<List<List<Position>>>, bbox: BoundingBox?) =
        MultiPolygon(coordinates, bbox)
}
