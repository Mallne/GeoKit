package cloud.mallne.geokit.geojson

import cloud.mallne.geokit.Boundary
import cloud.mallne.geokit.Vertex

object CalculationInterop {
    fun Position.toVertex() = Vertex(latitude, longitude)
    fun Point.toVertex() = Vertex(coordinates.latitude, coordinates.longitude)
    fun Vertex.toPosition(): Position = Position(longitude, latitude)

    fun BoundingBox.toBoundary(): Boundary = Boundary(this.southwest.toVertex(), this.northeast.toVertex())
    fun Boundary.toBoundingBox(): BoundingBox = BoundingBox(this.southWest.toPosition(), this.northEast.toPosition())
}