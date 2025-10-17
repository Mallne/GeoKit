package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.sytems.CRS

object Coordinates {
    fun convert(vertex: Vertex, from: CRS, to: CRS): Vertex {
        val intermediate = from.converter.to4327(vertex)
        return to.converter.from4326(intermediate)
    }
}