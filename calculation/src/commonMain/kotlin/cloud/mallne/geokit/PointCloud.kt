package cloud.mallne.geokit

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PointCloud(
    val points: List<Vertex>,
) : List<Vertex> {
    @Transient
    val bounds: Boundary = GeokitMeasurement.bbox(this)

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
