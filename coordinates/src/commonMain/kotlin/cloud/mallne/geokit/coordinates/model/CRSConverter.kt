package cloud.mallne.geokit.coordinates.model

import cloud.mallne.geokit.Vertex

interface CRSConverter {
    fun from4326(vertex: Vertex): Vertex
    fun to4327(vertex: Vertex): Vertex
}