package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.tokens.ast.expression.SteppedOperation
import cloud.mallne.geokit.coordinates.tokens.ast.expression.WKTCRSExpression
import kotlin.uuid.ExperimentalUuidApi

data class GeokitCoordinateConversionContext(
    val operation: SteppedOperation,
    var source: WKTCRSExpression,
    var target: WKTCRSExpression,
    private val logger: GeokitLogger? = null,
) {
    @OptIn(ExperimentalUuidApi::class)
    fun log(loggerScope: GeokitLogger.() -> Unit) {
        logger?.loggerScope()
    }
}
