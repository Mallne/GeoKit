package cloud.mallne.geokit

import cloud.mallne.geokit.CalculationExtensions.toDegrees
import cloud.mallne.geokit.NumericalExtensions.round
import cloud.mallne.units.Angle
import cloud.mallne.units.Angle.Companion.degrees
import cloud.mallne.units.Measure
import cloud.mallne.units.times
import kotlinx.serialization.Serializable
import kotlin.math.atan2

@Serializable
data class Vertex(
    val latitude: Double, //Y
    val longitude: Double, //X
) {

    override fun toString(): String {
        return "$latitude,$longitude"
    }

    fun directionalVectorTo(other: Vertex): DoubleArray {
        return GeokitMeasurement.directionalVectorOf(this, other)
    }

    fun vectorTo(other: Vertex): Vector {
        return Vector(this, other)
    }

    fun vectorFrom(other: Vertex): Vector {
        return Vector(other, this)
    }

    // Point subtraction results in a direction vector (FloatArray)
    operator fun minus(other: Vertex) = vectorFrom(other)
    operator fun plus(other: Vertex) = vectorTo(other)
    operator fun plus(other: DoubleArray) = Vertex(latitude + other[1], longitude + other[0])

    infix fun angleTo(other: Vertex): Measure<Angle> {
        val vector = directionalVectorTo(other)
        val radToX = atan2(vector[1], vector[0])
        val degToX = radToX.toDegrees()
        return (180f - degToX) * degrees //Because we dont want the X Axis but the positive Y Axis is North.
    }

    infix fun distanceTo(other: Vertex) = GeokitMeasurement.distance(this, other)
    infix fun intersection(shape: Shape) = GeokitMeasurement.intersectionVector(this, shape)
    infix fun inside(shape: Shape) = GeokitMeasurement.isPointInsidePolygon(this, shape)

    fun lookingAt(direction: Measure<Angle>, shape: Shape) =
        GeokitMeasurement.intersectionVectorInDirection(this, direction, shape)

    fun asString(decimals: Int = Int.MAX_VALUE) =
        "${this.latitude.round(decimals)}, ${longitude.round(decimals)}"

    companion object Composable
}