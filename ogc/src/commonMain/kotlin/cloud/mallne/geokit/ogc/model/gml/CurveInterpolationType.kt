package cloud.mallne.geokit.ogc.model.gml

/**
 * A representation of the literals of the enumeration '***Curve Interpolation Type***',
 * and utility methods for working with them.
 *
 *
 * CurveInterpolationType is a list of codes that may be used to identify the interpolation mechanisms specified by an
 * application schema.
 */
enum class CurveInterpolationType(
    val value: Int, val named: String, val literal: String
) {
    /**
     * The '***Linear***' literal object.
     */
    LINEAR(0, "linear", "linear"),

    /**
     * The '***Geodesic***' literal object.
     */
    GEODESIC(1, "geodesic", "geodesic"),

    /**
     * The '***Circular Arc3 Points***' literal object.
     */
    CIRCULAR_ARC3_POINTS(2, "circularArc3Points", "circularArc3Points"),

    /**
     * The '***Circular Arc2 Point With Bulge***' literal object.
     */
    CIRCULAR_ARC2_POINT_WITH_BULGE(3, "circularArc2PointWithBulge", "circularArc2PointWithBulge"),

    /**
     * The '***Circular Arc Center Point With Radius***' literal object.
     */
    CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS(4, "circularArcCenterPointWithRadius", "circularArcCenterPointWithRadius"),

    /**
     * The '***Elliptical***' literal object.
     */
    ELLIPTICAL(5, "elliptical", "elliptical"),

    /**
     * The '***Clothoid***' literal object.
     */
    CLOTHOID(6, "clothoid", "clothoid"),

    /**
     * The '***Conic***' literal object.
     */
    CONIC(7, "conic", "conic"),

    /**
     * The '***Polynomial Spline***' literal object.
     */
    POLYNOMIAL_SPLINE(8, "polynomialSpline", "polynomialSpline"),

    /**
     * The '***Cubic Spline***' literal object.
     */
    CUBIC_SPLINE(9, "cubicSpline", "cubicSpline"),

    /**
     * The '***Rational Spline***' literal object.
     */
    RATIONAL_SPLINE(10, "rationalSpline", "rationalSpline");


    /**
     * Returns the literal value of the enumerator, which is its string representation.
     */
    override fun toString(): String {
        return literal
    }
} //CurveInterpolationType

