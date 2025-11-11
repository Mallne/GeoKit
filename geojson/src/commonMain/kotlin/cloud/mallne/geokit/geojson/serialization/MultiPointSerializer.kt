package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.MultiPoint
import cloud.mallne.geokit.geojson.Position
import kotlinx.serialization.builtins.ListSerializer

internal object MultiPointSerializer :
    BaseGeometrySerializer<MultiPoint, List<Position>>(
        "MultiPoint",
        ListSerializer(Position.serializer()),
    ) {
    override fun getCoordinates(value: MultiPoint) = value.coordinates

    override fun construct(coordinates: List<Position>, bbox: BoundingBox?) =
        MultiPoint(coordinates, bbox)
}
