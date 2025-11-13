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
 * Represents a single Direct Position <gml:pos> element, containing space-separated coordinates.
 * e.g., <gml:pos>10.0 20.0</gml:pos>
 */
interface DirectPosition {
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    @XmlSerialName("pos", Namespaces.GML, Namespaces.Prefix.GML)
    val pos: List<Double>

    @Transient
    val vertex: Vertex
        get() = pos.toVertex().first()
}