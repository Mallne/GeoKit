package cloud.mallne.geokit.gml.model

import cloud.mallne.geokit.gml.SpaceSeparatedDoublesSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlValue

/** Text-only corner elements and Envelope wrapper. */
//@XmlSerialName(value = "lowerCorner", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
@SerialName("lowerCorner")
@Serializable
data class LowerCorner(
    @XmlValue
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val values: List<Double>
)