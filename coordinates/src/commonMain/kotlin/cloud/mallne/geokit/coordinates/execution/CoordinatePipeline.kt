package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.WKTCRSExpression

sealed interface CoordinatePipeline : Pipeline<AbstractCoordinate, AbstractCoordinate> {
    val given: WKTCRSExpression
    val wanted: WKTCRSExpression
    val reversed: Boolean
}
