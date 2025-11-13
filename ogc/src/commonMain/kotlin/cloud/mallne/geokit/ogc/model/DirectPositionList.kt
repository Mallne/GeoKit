package cloud.mallne.geokit.ogc.model

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.ogc.Extensions.toVertex
import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.SpaceSeparatedDoublesSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Represents a Direct Position List <gml:posList> element, containing space-separated coordinate tuples.
 * e.g., <gml:posList>10.0 20.0 30.0 40.0</gml:posList>
 */
interface DirectPositionList {
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    val posList: List<Double>

    @Transient
    val vertexes: List<Vertex>
        get() = posList.toVertex()
}