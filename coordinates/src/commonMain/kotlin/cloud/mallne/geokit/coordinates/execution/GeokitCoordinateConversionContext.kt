package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem
import cloud.mallne.geokit.coordinates.tokens.ast.expression.Operation
import kotlin.uuid.ExperimentalUuidApi

data class GeokitCoordinateConversionContext(
    val operation: Operation,
    var source: CoordinateReferenceSystem,
    var target: CoordinateReferenceSystem,
    private val logger: GeokitLogger? = null,
) {
    @OptIn(ExperimentalUuidApi::class)
    fun log(loggerScope: GeokitLogger.() -> Unit) {
        logger?.loggerScope()
    }
}
