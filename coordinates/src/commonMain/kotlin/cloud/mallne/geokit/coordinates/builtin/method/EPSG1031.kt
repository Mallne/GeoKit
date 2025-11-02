package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.builtin.EPSGParameters
import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod
import cloud.mallne.geokit.coordinates.execution.GeokitCoordinateConversionContext
import cloud.mallne.geokit.coordinates.execution.IdentityLocator.Companion.findParameter
import cloud.mallne.geokit.coordinates.execution.MethodDispatcher
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractOperationParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.OperationParameter

internal object EPSG1031: ExecutionDispatchMethod {
    override val commonNames: List<String> = listOf("Geocentric Translations (geog2D domain)")
    override val identity: String = "EPSG:1031"
    override fun execute(coordinate: AbstractCoordinate, parameters: List<AbstractParameter>, context: GeokitCoordinateConversionContext, dispatcher: MethodDispatcher, reverse: Boolean): AbstractCoordinate {
        fun Double.operation(d: Double): Double {
            return if (reverse) {
                this - d
            } else {
                this + d
            }
        }
        require(coordinate.is3D()) {
            "The coordinate is not 3D."
        }
        val xParam = parameters.findParameter(EPSGParameters.Tx) as? OperationParameter
        val yParam = parameters.findParameter(EPSGParameters.Ty) as? OperationParameter
        val zParam = parameters.findParameter(EPSGParameters.Tz) as? OperationParameter

        require(xParam != null && yParam != null && zParam != null) {
            "The parameters for the operation are missing."
        }

        val x = coordinate.x.operation(xParam.value)
        val y = coordinate.y.operation(yParam.value)
        val z = coordinate.z!!.operation(zParam.value)

        return LocalCoordinate(x, y, z)
    }
}