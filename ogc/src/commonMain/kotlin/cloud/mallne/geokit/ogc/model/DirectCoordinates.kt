package cloud.mallne.geokit.ogc.model

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.ogc.Extensions.toVertex
import cloud.mallne.geokit.ogc.SpaceSeparatedDoublesSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Represents a Direct Position List <gml:posList> element, containing space-separated coordinate tuples.
 * e.g., <gml:posList>10.0 20.0 30.0 40.0</gml:posList>
 */
interface DirectCoordinates {
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val coordinates: List<Double>

    @Transient
    val vertexes: List<Vertex>
        get() = coordinates.toVertex()
}