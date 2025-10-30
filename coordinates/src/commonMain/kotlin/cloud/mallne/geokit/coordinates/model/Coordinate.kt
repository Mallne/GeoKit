package cloud.mallne.geokit.coordinates.model

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem

data class Coordinate(
    val vertex: Vertex,
    val system: CoordinateReferenceSystem,
)
