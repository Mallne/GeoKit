package cloud.mallne.geokit.coordinates.tokens.ast.expression

import kotlin.math.pow
import kotlin.math.sqrt

data class Ellipsoid(
    val name: String,
    val semiMajorAxis: Double,
    val inverseFlattening: Double,
    val unit: LengthUnit? = null,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression {
    /**
     * The flattening (f) of the ellipsoid. f = 1 / (1/f)
     */
    val flattening get() = 1 / inverseFlattening
    /**
     * The square of the first eccentricity (e^2).
     * Formula: e^2 = 2f - f^2
     */
    val eccentricitySquared: Double
        get() = 2.0 * flattening - flattening.pow(2)
    /**
     * The first eccentricity (e).
     * Formula: e = sqrt(e^2)
     */
    val eccentricity: Double
        get() = sqrt(eccentricitySquared)
    /**
     * The semi-minor axis (b) of the ellipsoid, measured from the center to the pole.
     * Formula: b = a * (1 - f)
     */
    val semiMinorAxis: Double
        get() = semiMajorAxis * (1.0 - flattening)
}
