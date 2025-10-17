package cloud.mallne.geokit.coordinates.sytems

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.model.CRSConverter

data object EPSG4326 : CRS {
    override val name: String = "WGS84/ETRS89"
    override val code: String = "EPSG:4326"
    override val converter: CRSConverter = object : CRSConverter {
        override fun from4326(vertex: Vertex): Vertex = vertex
        override fun to4327(vertex: Vertex): Vertex = vertex
    }
}