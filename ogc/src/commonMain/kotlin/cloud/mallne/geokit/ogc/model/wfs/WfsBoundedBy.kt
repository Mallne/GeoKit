package cloud.mallne.geokit.ogc.model.wfs

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.Envelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/** Wrapper for wfs:boundedBy that contains a gml:Envelope. */
@XmlSerialName("boundedBy", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("boundedBy")
@Serializable
data class WfsBoundedBy(
    @XmlSerialName("Envelope", Namespaces.GML, Namespaces.Prefix.GML)
    @XmlElement(true)
    val envelope: Envelope? = null
)