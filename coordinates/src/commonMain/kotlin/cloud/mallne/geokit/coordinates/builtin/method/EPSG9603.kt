package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod
import cloud.mallne.geokit.coordinates.execution.GeokitCoordinateConversionContext
import cloud.mallne.geokit.coordinates.execution.MethodDispatcher
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractOperationParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter

internal object EPSG9603 : ExecutionDispatchMethod {
    override val commonNames: List<String> = listOf("Geocentric Translations (geog2D domain)")
    override val identity: String = "EPSG:9603"
    override fun execute(
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter>,
        context: GeokitCoordinateConversionContext,
        dispatcher: MethodDispatcher,
        reverse: Boolean
    ): AbstractCoordinate {
        val c0 = dispatcher.dispatchById(EPSG9659.identity, coordinate, reverse = true)
        val c1 = dispatcher.dispatchById(EPSG9602.identity, c0, reverse = reverse)
        val c2 = dispatcher.dispatchById(EPSG1031.identity, c1, parameters, reverse)
        val c3 = dispatcher.dispatchById(EPSG9602.identity, c2, reverse = !reverse)
        val c4 = dispatcher.dispatchById(EPSG9659.identity, c3)

        return c4
    }
}