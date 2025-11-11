package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.Point
import cloud.mallne.geokit.geojson.Position

internal object PointSerializer :
    BaseGeometrySerializer<Point, Position>("Point", Position.serializer()) {
    override fun getCoordinates(value: Point) = value.coordinates

    override fun construct(coordinates: Position, bbox: BoundingBox?) = Point(coordinates, bbox)
}
