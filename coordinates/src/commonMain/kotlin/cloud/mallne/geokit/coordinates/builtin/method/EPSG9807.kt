package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.builtin.EPSGParameters
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
    private const val INVERSE_TOLERANCE = 1e-12
    private const val MAX_INVERSE_ITERATIONS = 20
    private val PRINCIPAL_BOUNDARY_ROUNDOFF = 4.0 * (PI / 2.0).ulp
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
        // Use the projected target for forward conversion, or the projected source for reverse.
        val crs = if (!reverse) context.target as? ProjectedCrs else context.source as? ProjectedCrs
        require(crs != null) { "Transverse Mercator requires a Projected CRS to retrieve projection parameters." }
        val constants = calculateConstants(
            crs,
            parameters.filter { it is MapProjectionParameter } as List<MapProjectionParameter>,
        )
        return if (!reverse) {
            forward(coordinate, constants)
        } else {
            reverse(coordinate, constants)
        }
    }

    private data class Constants(
        val e: Double,
        val n: Double,
        val n2: Double,
        val n3: Double,
        val n4: Double,
        val B: Double,
        val phi_o: Double,
        val lambda_o: Double,
        val k_o: Double,
        val FE: Double,
        val FN: Double,
        val h_1: Double,
        val h_2: Double,
        val h_3: Double,
        val h_4: Double,
        val M_o: Double,
    )

    private fun forward(
        coordinate: AbstractCoordinate,
        constants: Constants,
    ): AbstractCoordinate {
        val (e, _, _, _, _, B, _, lambda_o, k_o, FE, FN, h_1, h_2, h_3, h_4, M_o) = constants
        val phi = coordinate.phi
        val lambda = coordinate.lambda
        require(phi.isFinite() && abs(phi) <= PI / 2.0 && lambda.isFinite()) {
            "EPSG:9807 forward requires finite longitude and latitude within [-pi/2, pi/2] " +
                "(latitude=$phi, longitude=$lambda)."
        }
        // All longitudes meet at a pole, including those outside the principal branch.
        if (abs(phi) == PI / 2.0) {
            return projectedCoordinate(FE, FN + k_o * (B * phi - M_o))
        }
        val wrappedDifference = normalizeLongitude(normalizeLongitude(lambda) - normalizeLongitude(lambda_o))
        // Unit conversion, wrapping and subtraction can move an exact 90-degree
        // boundary by a few ulps. This is roundoff allowance, not a wider branch.
        require(abs(wrappedDifference) <= PI / 2.0 + PRINCIPAL_BOUNDARY_ROUNDOFF) {
            "EPSG:9807 forward longitude difference must be within the principal [-pi/2, pi/2] branch " +
                "(longitude=$lambda, centralMeridian=$lambda_o)."
        }
        val deltaLambda = wrappedDifference.coerceIn(-PI / 2.0, PI / 2.0)

        val Q = asinh(tan(phi)) - (e * atanh(e * sin(phi)))
        val beta = atan(sinh(Q))
        val eta_0 = atanh(cos(beta) * sin(deltaLambda))
        require(eta_0.isFinite()) {
            "EPSG:9807 forward is singular (latitude=$phi, longitude=$lambda)."
        }
        // Equivalent principal-branch angle, without asin's loss of precision at the poles.
        val xi_0 = atan2(sin(beta), cos(beta) * cos(deltaLambda))
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

        return projectedCoordinate(E, N)
    }

    private fun projectedCoordinate(E: Double, N: Double): AbstractCoordinate {
        check(E.isFinite() && N.isFinite()) {
            "EPSG:9807 forward produced nonfinite projected coordinates (easting=$E, northing=$N)."
        }
        return LocalCoordinate(latitude = N, longitude = E)
    }

    private fun normalizeLongitude(longitude: Double): Double {
        val wrapped = longitude % (2.0 * PI)
        return when {
            wrapped > PI -> wrapped - 2.0 * PI
            wrapped < -PI -> wrapped + 2.0 * PI
            else -> wrapped
        }
    }

    private fun meridionalArcDistance(
        phi_o: Double,
        B: Double,
        e: Double,
        h_1: Double,
        h_2: Double,
        h_3: Double,
        h_4: Double,
    ): Double = if (phi_o == 0.0) {
        0.0
    } else if (phi_o == PI / 2.0) {
        B * (PI / 2.0)
    } else if (phi_o == -PI / 2.0) {
        B * (-PI / 2.0)
    } else {
        // Equivalent to atan(sinh(asinh(tan(phi_o)) - correction)), but avoids
        // the unstable tangent near the poles noted in EPSG guidance note 7-2.
        // atan2 also avoids losing the small polar distance in asin(sin(beta_o)).
        val correction = e * atanh(e * sin(phi_o))
        val xi_o0 = atan2(sin(phi_o) * cosh(correction) - sinh(correction), cos(phi_o))
        val xi_o1 = h_1 * sin(2.0 * xi_o0)
        val xi_o2 = h_2 * sin(4.0 * xi_o0)
        val xi_o3 = h_3 * sin(6.0 * xi_o0)
        val xi_o4 = h_4 * sin(8.0 * xi_o0)
        val xi_o = xi_o0 + xi_o1 + xi_o2 + xi_o3 + xi_o4
        B * xi_o
    }

    private fun reverse(
        coordinate: AbstractCoordinate,
        constants: Constants,
    ): AbstractCoordinate {
        val (e, n, n2, n3, n4, B, _, lambda_o, k_o, FE, FN, _, _, _, _, M_o) = constants
        val N = coordinate.northing
        val E = coordinate.easting
        check(N.isFinite() && E.isFinite()) {
            "EPSG:9807 inverse requires finite projected coordinates (easting=$E, northing=$N)."
        }
        val southPole = FN + k_o * (-B * (PI / 2.0) - M_o)
        val northPole = FN + k_o * (B * (PI / 2.0) - M_o)
        check(N in southPole..northPole) {
            "EPSG:9807 inverse northing is outside the principal branch (easting=$E, northing=$N)."
        }
        // Match the exact forward pole values before cancellation or trig roundoff can
        // turn the indeterminate pole longitude into NaN. Choose the central meridian.
        if (E == FE && (N == southPole || N == northPole)) {
            return LocalCoordinate(
                latitude = if (N == northPole) PI / 2.0 else -PI / 2.0,
                longitude = normalizeLongitude(lambda_o),
            )
        }
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
        check(xi_0.isFinite() && eta_0.isFinite() && abs(xi_0) <= PI / 2.0) {
            "EPSG:9807 inverse is singular or outside the principal branch (easting=$E, northing=$N)."
        }
        val sinhEta = sinh(eta_0)
        check(sinhEta.isFinite()) {
            "EPSG:9807 inverse is singular (easting=$E, northing=$N)."
        }
        val beta = atan2(sin(xi_0), hypot(sinhEta, cos(xi_0)))
        val Q = asinh(tan(beta))
        var Q_prime = Q
        var converged = false
        for (i in 0 until MAX_INVERSE_ITERATIONS) {
            val Q_doublePrime = Q + (e * atanh(e * tanh(Q_prime)))
            val change = abs(Q_doublePrime - Q_prime)
            Q_prime = Q_doublePrime
            if (change <= INVERSE_TOLERANCE) {
                converged = true
                break
            }
        }
        check(converged) {
            "EPSG:9807 inverse latitude did not converge within $MAX_INVERSE_ITERATIONS iterations " +
                "(easting=$E, northing=$N, Q=$Q_prime)."
        }
        val phi = atan(sinh(Q_prime))
        val lambda = normalizeLongitude(normalizeLongitude(lambda_o) + atan2(sinhEta, cos(xi_0)))
        check(phi.isFinite() && lambda.isFinite()) {
            "EPSG:9807 inverse produced nonfinite geographic coordinates (easting=$E, northing=$N)."
        }
        return LocalCoordinate(latitude = phi, longitude = lambda)
    }

    private fun calculateConstants(crs: ProjectedCrs, parameters: List<MapProjectionParameter>): Constants {
        val phi_oParam = parameters.findParameter(EPSGParameters.Phi_o) as? MapProjectionParameter
        val lambda_oParam = parameters.findParameter(EPSGParameters.Lambda_o) as? MapProjectionParameter
        val k_oParam = parameters.findParameter(EPSGParameters.K_o) as? MapProjectionParameter
        val FEParam = parameters.findParameter(EPSGParameters.FE) as? MapProjectionParameter
        val FNParam = parameters.findParameter(EPSGParameters.FN) as? MapProjectionParameter

        require(phi_oParam != null && lambda_oParam != null && k_oParam != null && FEParam != null && FNParam != null) {
            "Not all parameters are present!"
        }

        val phi_o = phi_oParam.convert()
        val lambda_o = lambda_oParam.convert()
        val k_o = k_oParam.convert()
        val FE = FEParam.convert()
        val FN = FNParam.convert()

        val ellipsoid = crs.base.constraints.ellipsoid
        val a = ellipsoid.semiMajorAxis * (ellipsoid.unit?.conversionFactor ?: 1.0)
        val f = ellipsoid.flattening
        val e = ellipsoid.eccentricity

        val n = f / (2.0 - f)
        val n2 = n * n
        val n3 = n2 * n
        val n4 = n3 * n
        val B = (a / (1.0 + n)) * (1.0 + n2 / 4.0 + n4 / 64.0)

        val h_1 = n / 2.0 - (2.0 / 3.0) * n2 + (5.0 / 16.0) * n3 + (41.0 / 180.0) * n4
        val h_2 = (13.0 / 48.0) * n2 - (3.0 / 5.0) * n3 + (557.0 / 1440.0) * n4
        val h_3 = (61.0 / 240.0) * n3 - (103.0 / 140.0) * n4
        val h_4 = (49561.0 / 161280.0) * n4

        val M_o = meridionalArcDistance(phi_o, B, e, h_1, h_2, h_3, h_4)
        return Constants(e, n, n2, n3, n4, B, phi_o, lambda_o, k_o, FE, FN, h_1, h_2, h_3, h_4, M_o)
    }
}
