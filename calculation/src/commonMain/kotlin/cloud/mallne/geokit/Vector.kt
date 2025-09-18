package cloud.mallne.geokit

import cloud.mallne.geokit.CalculationExtensions.magnitude
import kotlinx.serialization.Serializable

@Serializable
data class Vector(
    val origin: Vertex,
    val direction: DoubleArray
) {
    val destination: Vertex
        get() = GeokitMeasurement.destination(origin, direction)

    constructor(origin: Vertex, destination: Vertex) : this(
        origin,
        origin.directionalVectorTo(destination)
    )

    init {
        require(direction.size == 2) { "Vector must be in two-dimensional space." }
    }

    infix fun dot(other: Vector) =
        this.direction.foldIndexed(0.0) { i, acc, cur -> acc + cur * other.direction[i] }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other != null && this::class != other::class) return false

        other as Vector

        if (origin != other.origin) return false
        if (!direction.contentEquals(other.direction)) return false

        return true
    }

    fun length() = GeokitMeasurement.distance(origin, destination)
    fun fastLength() = direction.magnitude()
    override fun hashCode(): Int {
        var result = origin.hashCode()
        result = 31 * result + direction.contentHashCode()
        return result
    }
}
