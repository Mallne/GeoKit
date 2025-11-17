package cloud.mallne.geokit.interop

import cloud.mallne.geokit.interop.GmlExtensions.toGml
import cloud.mallne.geokit.interop.GmlExtensions.toGmlPrimitive
import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import org.maplibre.spatialk.geojson.Position

object GmlExtensions {
    fun Position.toGmlPrimitive(): List<Double> = listOf(latitude, longitude)
    fun List<Position>.toGmlPrimitive(): List<Double> = this.flatMap { it.toGmlPrimitive() }
    fun Position.toGml(): DirectPositionType = DirectPositionType(this.toGmlPrimitive())
    fun List<Position>.toGml(): DirectPositionType = DirectPositionType(this.toGmlPrimitive())
    fun List<List<Position>>.toGml(): List<DirectPositionType> = this.map { it.toGml() }
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