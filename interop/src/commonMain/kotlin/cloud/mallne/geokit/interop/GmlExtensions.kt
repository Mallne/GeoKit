package cloud.mallne.geokit.interop

import cloud.mallne.geokit.geojson.Position

object GmlExtensions {
    fun Position.toGml(): List<Double> = listOf(latitude, longitude)
    fun List<Position>.toGml(): List<Double> = this.flatMap { it.toGml() }
    fun List<Double>.toGeoJson(): List<Position> {
        require(this.size % 2 == 0) {
            "Vertexlist must have an even number of values"
        }
        val l = mutableListOf<Position>()
        for (i in 0 until (this.size - 1)) {
            if (i % 2 == 0) {
                val thisOne = this[i]
                val thisTwo = this[i + 1]
                val vertex = Position(thisTwo, thisOne)
                l.add(vertex)
            }
        }
        return l
    }
}