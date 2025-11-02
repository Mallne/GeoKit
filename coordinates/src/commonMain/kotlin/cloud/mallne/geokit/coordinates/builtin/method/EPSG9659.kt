package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod
import cloud.mallne.geokit.coordinates.execution.GeokitCoordinateConversionContext
import cloud.mallne.geokit.coordinates.execution.MethodDispatcher
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractOperationParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter

internal object EPSG9659 : ExecutionDispatchMethod {
    override val commonNames: List<String> =
        listOf("Geographic 2D to Geographic 3D", "Geographic 3D to Geographic 2D", "Geographic 3D to 2D conversions")
    override val identity: String = "EPSG:9659"

    /**
     * The forward case is trivial. A geographic 2D CRS is derived from a 3-dimensional geographic coordinate
     * reference system comprising of geodetic latitude, geodetic longitude and ellipsoidal height by the simple
     * expedient of dropping the height.
     *
     * The reverse conversion, from 2D to 3D, is indeterminate. It is however a requirement when a geographic 2D
     * coordinate reference system is to be transformed using a geocentric method which is 3-dimensional (see
     * section 4.3.1). In practice an artificial ellipsoidal height is created and appended to the geographic 2D
     * coordinate reference system to create a geographic 3D coordinate reference system referenced to the same
     * geodetic datum. The assumed ellipsoidal height is usually either set to the gravity-related height of a position
     * in a compound coordinate reference system, or set to zero. As long as the height chosen is within a few
     * kilometres of sea level, no error will be induced into the horizontal position resulting from the later
     * geocentric transformation; the vertical coordinate will however be meaningless.
     */
    override fun execute(
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter>,
        context: GeokitCoordinateConversionContext,
        dispatcher: MethodDispatcher,
        reverse: Boolean
    ): AbstractCoordinate {
        return if (!reverse) {
            LocalCoordinate(coordinate.latitude, coordinate.longitude)
        } else {
            LocalCoordinate(coordinate.latitude, coordinate.longitude, coordinate.altitude ?: 0.0)
        }
    }
}