package cloud.mallne.geokit

import cloud.mallne.units.Angle
import cloud.mallne.units.Angle.Companion.radians
import cloud.mallne.units.Length
import cloud.mallne.units.Length.Companion.meters
import cloud.mallne.units.Measure
import cloud.mallne.units.times

object GeokitConversion {
    /**
     * Convert a distance measurement (assuming a spherical Earth) from radians to a more friendly
     * unit.
     */
    fun angleToLength(angle: Measure<Angle>): Measure<Length> {
        return (angle `as` radians).amount * GeokitConstants.EARTH_RADIUS
    }

    /**
     * Convert a distance measurement (assuming a spherical Earth) from a real-world unit into
     * radians.
     */
    fun lengthToAngle(distance: Measure<Length>): Measure<Angle> {
        return (((distance `in` meters) / (GeokitConstants.EARTH_RADIUS `in` meters))) * radians
    }

    fun List<List<Vertex>>.toPointCloud(): PointCloud {
        return PointCloud(this.flatten())
    }
}