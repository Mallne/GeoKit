package cloud.mallne.geokit.ogc.model

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.segment.AbstractSegment
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("segments", Namespaces.GML, Namespaces.Prefix.GML)
@Serializable
data class CurveSegments(
    // List of polymorphic segments. @XmlElement(true) is used for polymorphism in collections.
    @XmlElement
    val segments: List<AbstractSegment>
)