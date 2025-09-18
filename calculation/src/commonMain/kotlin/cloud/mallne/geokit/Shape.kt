package cloud.mallne.geokit

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Shape(
    private val points: List<Vertex>
) : List<Vertex> {
    @Transient
    val bounds: Boundary = GeokitMeasurement.bbox(this)

    @Transient
    val direction: List<DoubleArray> = run {
        val l = mutableListOf<DoubleArray>()
        for (i in points.indices) {
            val to = if (i == points.size - 1) points.first() else points[i + 1]
            val vc = points[i].directionalVectorTo(to)
            l.add(vc)
        }
        l.toList()
    }

    @Transient
    val origin: Vertex = points.first()

    fun asVectors(): List<Vector> {
        val l = mutableListOf<Vector>()
        for (i in points.indices) {
            l.add(Vector(points[i], direction[i]))
        }
        return l.toList()
    }

    override val size: Int
        get() = points.size

    override fun isEmpty(): Boolean = points.isEmpty()
    override fun contains(element: Vertex): Boolean = points.contains(element)
    override fun iterator(): Iterator<Vertex> = points.iterator()
    override fun containsAll(elements: Collection<Vertex>): Boolean = points.containsAll(elements)
    override fun get(index: Int): Vertex = points[index]
    override fun indexOf(element: Vertex): Int = points.indexOf(element)
    override fun lastIndexOf(element: Vertex): Int = points.lastIndexOf(element)
    override fun listIterator(): ListIterator<Vertex> = points.listIterator()
    override fun listIterator(index: Int): ListIterator<Vertex> = points.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<Vertex> = points.subList(fromIndex, toIndex)
}
