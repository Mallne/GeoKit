package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod
import cloud.mallne.geokit.coordinates.execution.GeokitCoordinateConversionContext
import cloud.mallne.geokit.coordinates.execution.MethodDispatcher
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.GeodeticCoordinateReferenceSystem
import kotlin.math.*

internal object EPSG9602 : ExecutionDispatchMethod {
    override val commonNames: List<String> =
        listOf("Geographic 3D to Geocentric", "Geocentric to Geographic 3D", "Geographic/Geocentric conversions")
    override val identity: String = "EPSG:9602"

    /**
     * Latitude, ϕ, and Longitude, λ, and ellipsoidal height, h, in terms of a 3-dimensional geographic coordinate
     * reference system may be expressed in terms of a geocentric (earth centred) Cartesian coordinate reference
     * system X, Y, Z with the Z axis corresponding with the earth’s rotation axis positive northwards, the X axis
     * through the intersection of the prime meridian and equator, and the Y axis through the intersection of the
     * equator with longitude 90°E. The geographic and geocentric systems are based on the same geodetic datum.
     * Geocentric coordinate reference systems are conventionally taken to be defined with the X axis through the
     * intersection of the Greenwich meridian and equator. This requires that the equivalent geographic coordinate
     * reference system be based on the Greenwich meridian. In application of the formulas below, geographic
     * coordinate reference systems based on a non-Greenwich prime meridian should first be transformed to their
     * Greenwich equivalent. Geocentric coordinates X, Y and Z take their units from the units for the ellipsoid
     * axes (a and b). As it is conventional for X, Y and Z to be in metres, if the ellipsoid axis dimensions are given
     * in another linear unit they should first be converted to metres.
     *
     * TODO handle the case for differing units and non Greenwich prime meridians
     */
    override fun execute(
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter>,
        context: GeokitCoordinateConversionContext,
        dispatcher: MethodDispatcher,
        reverse: Boolean
    ): AbstractCoordinate {
        require(coordinate.is3D()) {
            "Coordinate is not 3D. This Operation however requires a 3D coordinate."
        }
        val coordinateSystem =
            if (!reverse) context.source as? GeodeticCoordinateReferenceSystem else context.target as? GeodeticCoordinateReferenceSystem
        require(coordinateSystem != null) {
            "The coordinate system for the Operation has to be a GeodeticCoordinateReferenceSystem."
        }

        return if (!reverse) {
            forward(coordinate, coordinateSystem)
        } else {
            reverse(coordinate, coordinateSystem)
        }
    }

    private fun forward(
        coordinate: AbstractCoordinate,
        coordinateSystem: GeodeticCoordinateReferenceSystem
    ): LocalCoordinate {
        val phi = coordinate.phi
        val lambda = coordinate.lambda
        val h = coordinate.h!!

        // Extract ellipsoid parameters
        val a = coordinateSystem.system.ellipsoid.semiMajorAxis
        val e2 = coordinateSystem.system.ellipsoid.eccentricitySquared

        // Calculate derived parameters
        val sinPhi = sin(phi)
        val cosPhi = cos(phi)
        val sinLambda = sin(lambda)
        val cosLambda = cos(lambda)

        // Calculate prime vertical radius
        val v = a / sqrt(1.0 - e2 * sinPhi * sinPhi)

        // Calculate cartesian coordinates
        val x = (v + h) * cosPhi * cosLambda
        val y = (v + h) * cosPhi * sinLambda
        val z = ((1.0 - e2) * v + h) * sinPhi

        return LocalCoordinate(x, y, z)
    }

    private fun reverse(
        coordinate: AbstractCoordinate,
        coordinateSystem: GeodeticCoordinateReferenceSystem
    ): LocalCoordinate {
        val x = coordinate.x
        val y = coordinate.y
        val z = coordinate.z!!

        val lambda = atan2(y, x)
        val p = hypot(x, y)
        val e2 = coordinateSystem.system.ellipsoid.eccentricitySquared
        val a = coordinateSystem.system.ellipsoid.semiMajorAxis
        val b = coordinateSystem.system.ellipsoid.semiMinorAxis

        // Handle special case for points on the Z-axis (at the poles)
        if (p < 1e-6) {
            val semiMinorAxis = a * sqrt(1.0 - e2) // b = a * sqrt(1-e^2)
            val phi = if (z > 0) PI / 2.0 else -PI / 2.0
            val h = abs(z) - semiMinorAxis
            return LocalCoordinate(phi, 0.0, h)
        }

        val epsilon = e2 / (1 - e2)
        // Calculate intermediate parametric latitude q
        val q = atan2(b * a, p * b)
        // Calculate components for the final latitude formula
        val sinQ = sin(q)
        val cosQ = cos(q)

        val numerator = z + epsilon * b * sinQ.pow(3)
        val denominator = p - e2 * a * cosQ.pow(3)

        // Calculate final Geodetic Latitude (phi)
        val phi = atan2(numerator, denominator)

        // Calculate prime vertical radius (v or N) using the final phi
        val sinPhi = sin(phi)
        val v = a / sqrt(1.0 - e2 * sinPhi * sinPhi)

        // Calculate final Ellipsoidal Height (h)
        val h = (p / cos(phi)) - v

        return LocalCoordinate(phi, lambda, h)
    }
}