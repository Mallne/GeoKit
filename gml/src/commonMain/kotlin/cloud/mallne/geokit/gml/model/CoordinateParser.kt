package cloud.mallne.geokit.gml.model

import cloud.mallne.geokit.Vertex

internal object CoordinateParser {
    fun parse(str: String): List<Double> = str.split(Regex("\\s+")).map { it.toDouble() }
    internal fun List<Double>.toVertex(): List<Vertex> {
        require(this.size % 2 == 0) {
            "Vertexlist must have an even number of values"
        }
        val l = mutableListOf<Vertex>()
        for (i in 0 until (this.size - 1)) {
            if (i % 2 == 0) {
                val thisOne = this[i]
                val thisTwo = this[i + 1]
                val vertex = Vertex(thisOne, thisTwo)
                l.add(vertex)
            }
        }
        return l
    }
}