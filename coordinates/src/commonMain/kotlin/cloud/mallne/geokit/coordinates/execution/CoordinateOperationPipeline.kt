package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem
import cloud.mallne.geokit.coordinates.tokens.ast.expression.Operation

sealed interface CoordinateOperationPipeline<T : Operation> : CoordinatePipeline {
    override val given: CoordinateReferenceSystem
    override val wanted: CoordinateReferenceSystem
    val operation: T
}
