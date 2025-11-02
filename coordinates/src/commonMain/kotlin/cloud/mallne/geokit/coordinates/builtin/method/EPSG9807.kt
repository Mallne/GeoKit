package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod
import cloud.mallne.geokit.coordinates.execution.GeokitCoordinateConversionContext
import cloud.mallne.geokit.coordinates.execution.MethodDispatcher
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter

internal object EPSG9807 : ExecutionDispatchMethod {
    override val commonNames: List<String> = listOf("Transverse Mercator")
    override val identity: String = "EPSG:9807"
    override fun execute(
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter>,
        context: GeokitCoordinateConversionContext,
        dispatcher: MethodDispatcher,
        reverse: Boolean
    ): AbstractCoordinate {

    }
}