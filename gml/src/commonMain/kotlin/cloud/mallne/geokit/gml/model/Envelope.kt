package cloud.mallne.geokit.gml.model

import cloud.mallne.geokit.gml.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/** Bounding box envelope using 2 corner positions. */
@XmlSerialName("Envelope", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Envelope")
@Serializable
data class Envelope(
    @XmlElement(true)
    val lowerCorner: LowerCorner,
    @XmlElement(true)
    val upperCorner: UpperCorner,
    val srsName: String? = null,
    val id: String? = null,
)