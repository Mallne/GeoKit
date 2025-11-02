package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem
import cloud.mallne.geokit.coordinates.tokens.ast.expression.Operation

sealed interface CoordinateOperationPipeline<T : Operation> : Pipeline<AbstractCoordinate, AbstractCoordinate> {
    val reversed: Boolean
    val operation: T
    val given: CoordinateReferenceSystem
    val wanted: CoordinateReferenceSystem
}
