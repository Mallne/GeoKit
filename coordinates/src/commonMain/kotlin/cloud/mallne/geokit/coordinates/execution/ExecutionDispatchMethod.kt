package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter

interface ExecutionDispatchMethod: IdentityLocator {
    fun execute(
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter>,
        context: GeokitCoordinateConversionContext,
        dispatcher: MethodDispatcher,
        reverse: Boolean
            ): AbstractCoordinate
}