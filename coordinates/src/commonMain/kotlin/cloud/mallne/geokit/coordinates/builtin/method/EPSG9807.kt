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
import kotlin.properties.Delegates

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
        calculateConstants(crs, parameters.filter { it is MapProjectionParameter } as List<MapProjectionParameter>)
        return if (!reverse) {
            forward(coordinate)
        } else {
            reverse(coordinate)
        }
    }

    private var f by Delegates.notNull<Double>()
    private var e by Delegates.notNull<Double>()
    private var n by Delegates.notNull<Double>()
    private var n2 by Delegates.notNull<Double>()
    private var n3 by Delegates.notNull<Double>()
    private var n4 by Delegates.notNull<Double>()
    private var B by Delegates.notNull<Double>()
    private var phi_o by Delegates.notNull<Double>()
    private var lambda_o by Delegates.notNull<Double>()
    private var k_o by Delegates.notNull<Double>()
    private var FE by Delegates.notNull<Double>()
    private var FN by Delegates.notNull<Double>()
    private var h_1 by Delegates.notNull<Double>()
    private var h_2 by Delegates.notNull<Double>()
    private var h_3 by Delegates.notNull<Double>()
    private var h_4 by Delegates.notNull<Double>()
    private var M_o by Delegates.notNull<Double>()

    private fun forward(
        coordinate: AbstractCoordinate,
    ): AbstractCoordinate {
        val phi = coordinate.phi
        val lambda = coordinate.lambda

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

    private fun meridionalArcDistance(): Double = if (phi_o `~` 0.0) {
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

    private fun reverse(
        coordinate: AbstractCoordinate,
    ): AbstractCoordinate {
        val N = coordinate.northing
        val E = coordinate.easting
        val h_1_prime = n / 2.0 - (2.0 / 3.0) * n2 + (37.0 / 96.0) * n3 - (1.0 / 360.0) * n4
        val h_2_prime = (1.0 / 48.0) * n2 + (1.0 / 15.0) * n3 - (437.0 / 1440.0) * n4
        val h_3_prime = (17.0 / 480.0) * n3 - (37.0 / 840.0) * n4
        val h_4_prime = (4397.0 / 161280.0) * n4

        val eta = (E - FE) / (B * k_o)
        val xi = ((N - FN) + k_o * M_o) / (B * k_o)
        val xi_1 = h_1_prime * sin(2.0 * xi) * cosh(2.0 * eta)
        val xi_2 = h_2_prime * sin(4.0 * xi) * cosh(4.0 * eta)
        val xi_3 = h_3_prime * sin(6.0 * xi) * cosh(6.0 * eta)
        val xi_4 = h_4_prime * sin(8.0 * xi) * cosh(8.0 * eta)
        val xi_0 = xi - (xi_1 + xi_2 + xi_3 + xi_4)
        val eta_1 = h_1_prime * cos(2.0 * xi) * sinh(2.0 * eta)
        val eta_2 = h_2_prime * cos(4.0 * xi) * sinh(4.0 * eta)
        val eta_3 = h_3_prime * cos(6.0 * xi) * sinh(6.0 * eta)
        val eta_4 = h_4_prime * cos(8.0 * xi) * sinh(8.0 * eta)
        val eta_0 = eta - (eta_1 + eta_2 + eta_3 + eta_4)
        val beta = asin(sin(xi_0) / cosh(eta_0))
        var Q_prime = asinh(tan(beta))
        var Q_doublePrime: Double
        // Iteration to achieve high accuracy for $\phi$
        for (i in 0 until 10) { // Max 10 iterations for guaranteed convergence
            Q_doublePrime = Q_prime + (e * atanh(e * tanh(Q_prime)))

            // Check for convergence
            if (Q_doublePrime `~` Q_prime) {
                break
            }
            Q_prime = Q_doublePrime
        }
        val phi = atan(sinh(Q_prime))
        val lambda = lambda_o + asin(tanh(eta_0) / cos(beta))
        return LocalCoordinate(latitude = phi, longitude = lambda)
    }

    private fun calculateConstants(crs: ProjectedCrs, parameters: List<MapProjectionParameter>) {
        val phi_oParam = parameters.findParameter(EPSGParameters.Phi_o) as? MapProjectionParameter
        val lambda_oParam = parameters.findParameter(EPSGParameters.Lambda_o) as? MapProjectionParameter
        val k_oParam = parameters.findParameter(EPSGParameters.K_o) as? MapProjectionParameter
        val FEParam = parameters.findParameter(EPSGParameters.FE) as? MapProjectionParameter
        val FNParam = parameters.findParameter(EPSGParameters.FN) as? MapProjectionParameter

        require(phi_oParam != null && lambda_oParam != null && k_oParam != null && FEParam != null && FNParam != null) {
            "Not all parameters are present!"
        }

        phi_o = phi_oParam.convert()
        lambda_o = lambda_oParam.convert()
        k_o = k_oParam.convert()
        FE = FEParam.convert()
        FN = FNParam.convert()

        val ellipsoid = crs.base.constraints.ellipsoid
        val a = ellipsoid.semiMajorAxis
        f = ellipsoid.flattening
        e = ellipsoid.eccentricity

        n = f / (2.0 - f)
        n2 = n * n
        n3 = n2 * n
        n4 = n3 * n
        B = (a / (1.0 + n)) * (1.0 + n2 / 4.0 + n4 / 64.0)

        h_1 = n / 2.0 - (2.0 / 3.0) * n2 + (5.0 / 16.0) * n3 + (41.0 / 180.0) * n4
        h_2 = (13.0 / 48.0) * n2 - (3.0 / 5.0) * n3 + (557.0 / 1440.0) * n4
        h_3 = (61.0 / 240.0) * n3 - (103.0 / 140.0) * n4
        h_4 = (49561.0 / 161280.0) * n4

        M_o = meridionalArcDistance()
    }
}