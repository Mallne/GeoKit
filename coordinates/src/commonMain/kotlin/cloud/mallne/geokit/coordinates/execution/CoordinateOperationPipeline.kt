package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.model.Coordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.Operation

sealed interface CoordinateOperationPipeline<T : Operation> : Pipeline<Vertex, Coordinate> {
    val reversed: Boolean
    val operation: T
}
