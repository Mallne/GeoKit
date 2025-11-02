package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.builtin.EPSGParameters
import cloud.mallne.geokit.coordinates.execution.CalculationExtensions.`~`
import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod
import cloud.mallne.geokit.coordinates.execution.GeokitCoordinateConversionContext
import cloud.mallne.geokit.coordinates.execution.IdentityLocator.Companion.findParameter
import cloud.mallne.geokit.coordinates.execution.MethodDispatcher
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.MapProjectionParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.ProjectedCrs
import kotlin.math.*

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
        // Transverse Mercator is typically a Geographic -> Projected conversion (Forward) or vice versa (Reverse).
        // The parameters are always taken from the Target CRS definition (the Projected CRS).
        val crs = if (!reverse) context.target as? ProjectedCrs else context.source as? ProjectedCrs
        require(crs != null) { "Transverse Mercator requires a Projected CRS to retrieve projection parameters." }
        return if (!reverse) {
            forward(
                crs = crs,
                parameters = parameters.filter { it is MapProjectionParameter } as List<MapProjectionParameter>,
                coordinate = coordinate
            )
        } else {
            reverse(
                crs = crs,
                parameters = parameters.filter { it is MapProjectionParameter } as List<MapProjectionParameter>,
                coordinate = coordinate
            )
        }

    }

    fun forward(
        crs: ProjectedCrs,
        parameters: List<MapProjectionParameter>,
        coordinate: AbstractCoordinate,
    ): AbstractCoordinate {
        val phi_oParam = parameters.findParameter(EPSGParameters.Phi_o) as? MapProjectionParameter
        val lambda_oParam = parameters.findParameter(EPSGParameters.Lambda_o) as? MapProjectionParameter
        val k_oParam = parameters.findParameter(EPSGParameters.K_o) as? MapProjectionParameter
        val FEParam = parameters.findParameter(EPSGParameters.FE) as? MapProjectionParameter
        val FNParam = parameters.findParameter(EPSGParameters.FN) as? MapProjectionParameter

        require(phi_oParam != null && lambda_oParam != null && k_oParam != null && FEParam != null && FNParam != null) {
            "Not all parameters are present!"
        }

        val phi_o = phi_oParam.convert()
        val phi = coordinate.latitude
        val lambda_o = lambda_oParam.convert()
        val lambda = coordinate.longitude
        val k_o = k_oParam.convert()
        val FE = FEParam.convert()
        val FN = FNParam.convert()

        val ellipsoid = crs.base.constraints.ellipsoid
        val a = ellipsoid.semiMajorAxis
        val e = ellipsoid.eccentricity
        val f = ellipsoid.flattening

        val n = f / (2 - f)
        val n2 = n * n
        val n3 = n2 * n
        val n4 = n3 * n
        val B = (a / (1.0 + n)) * (1.0 + n2 / 4.0 + n4 / 64.0)

        val h_1 = n / 2.0 - (2.0 / 3.0) * n2 + (5.0 / 16.0) * n3 + (41.0 / 180.0) * n4
        val h_2 = (13.0 / 48.0) * n2 - (3.0 / 5.0) * n3 + (557.0 / 1440.0) * n4
        val h_3 = (61.0 / 240.0) * n3 - (103.0 / 140.0) * n4
        val h_4 = (49561.0 / 161280.0) * n4

        val M_o = if (phi_o `~` 0.0) {
            0.0
        } else if (phi_o `~` (PI / 2.0)) {
            B * (PI / 2.0)
        } else if (phi_o `~` (-PI / 2.0)) {
            B * (-PI / 2.0)
        } else {
            val Q_o = asinh(tan(phi_o)) - (e * atanh(e * sin(phi_o)))
            val beta_o = atan(sinh(Q_o))
            val xi_o0 = asin(sin(beta_o))
            val xi_o1 = h_1 * sin(2.0 * xi_o0)
            val xi_o2 = h_2 * sin(4.0 * xi_o0)
            val xi_o3 = h_3 * sin(6.0 * xi_o0)
            val xi_o4 = h_4 * sin(8.0 * xi_o0)
            val xi_o = xi_o0 + xi_o1 + xi_o2 + xi_o3 + xi_o4
            B * xi_o
        }

        val Q = asinh(tan(phi)) - (e * atanh(e * sin(phi)))
        val beta = atan(sinh(Q))
        val eta_0 = atanh(cos(beta) * sin(lambda - lambda_o))
        val xi_0 = asin(sin(beta) * cosh(eta_0))
        val xi_1 = h_1 * sin(2.0 * xi_0) * cosh(2.0 * eta_0)
        val xi_2 = h_2 * sin(4.0 * xi_0) * cosh(4.0 * eta_0)
        val xi_3 = h_3 * sin(6.0 * xi_0) * cosh(6.0 * eta_0)
        val xi_4 = h_4 * sin(8.0 * xi_0) * cosh(8.0 * eta_0)
        val eta_1 = h_1 * cos(2.0 * xi_0) * sinh(2.0 * eta_0)
        val eta_2 = h_2 * cos(4.0 * xi_0) * sinh(4.0 * eta_0)
        val eta_3 = h_3 * cos(6.0 * xi_0) * sinh(6.0 * eta_0)
        val eta_4 = h_4 * cos(8.0 * xi_0) * sinh(8.0 * eta_0)
        val xi = xi_0 + xi_1 + xi_2 + xi_3 + xi_4
        val eta = eta_0 + eta_1 + eta_2 + eta_3 + eta_4

        val E = FE + k_o * B * eta
        val N = FN + k_o * (B * xi - M_o)

        return LocalCoordinate(latitude = N, longitude = E)
    }

    fun reverse(
        crs: ProjectedCrs,
        parameters: List<MapProjectionParameter>,
        coordinate: AbstractCoordinate,
    ): AbstractCoordinate {

    }
}