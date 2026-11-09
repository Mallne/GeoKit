package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.*
import kotlin.math.PI

/** Normalize CRS input to the radians/metres expected by execution methods. */
internal fun CoordinateReferenceSystem.normalizeInput(coordinate: AbstractCoordinate): AbstractCoordinate {
    val cs = when (this) {
        is GeodeticCoordinateReferenceSystem -> coordinateSystem
        is ProjectedCrs -> coordinateSystem
        else -> null
    } as? SpatialCS ?: return getDatumUnit()?.let { coordinate.autoConvert(it) } ?: coordinate.toRad()

    val angular = cs.type == SpatialCSType.ELLIPSOIDAL
    fun factor(matches: (AxisDirection) -> Boolean, vertical: Boolean = false): Double {
        // Axis-specific units take precedence over the CS-level default. Match by
        // direction, not WKT order: LocalCoordinate stores geographic/projected
        // coordinates as latitude/northing, longitude/easting, height, but XYZ as XYZ.
        val unit = cs.spatialAxis.firstOrNull { matches(it.direction) }?.unit
            ?: cs.unit?.takeUnless { vertical && it is AngleUnit }
        return unit?.conversionFactor ?: if (angular && !vertical) PI / 180.0 else 1.0
    }

    val first = factor({ it is AxisDirection.NORTH || it is AxisDirection.SOUTH || it == AxisDirection.GEOCENTRICX })
    val second = factor({ it == AxisDirection.EAST || it == AxisDirection.WEST || it == AxisDirection.GEOCENTRICY })
    val third = factor({ it == AxisDirection.UP || it == AxisDirection.DOWN || it == AxisDirection.GEOCENTRICZ }, vertical = true)
    return LocalCoordinate(
        latitude = coordinate.latitude * first,
        longitude = coordinate.longitude * second,
        altitude = coordinate.altitude?.let { it * third },
    )
}
