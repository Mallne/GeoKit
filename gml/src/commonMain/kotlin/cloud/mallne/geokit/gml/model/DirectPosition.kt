package cloud.mallne.geokit.gml.model

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.gml.SpaceSeparatedDoublesSerializer
import cloud.mallne.geokit.gml.model.CoordinateParser.toVertex
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Represents a single Direct Position <gml:pos> element, containing space-separated coordinates.
 * e.g., <gml:pos>10.0 20.0</gml:pos>
 */
interface DirectPosition {
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val pos: List<Double>

    @Transient
    val vertex: Vertex
        get() = pos.toVertex().first()
}