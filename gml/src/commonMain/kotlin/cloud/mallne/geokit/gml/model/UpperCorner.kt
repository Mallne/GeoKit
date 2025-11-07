package cloud.mallne.geokit.gml.model

import cloud.mallne.geokit.gml.SpaceSeparatedDoublesSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlValue

//@XmlSerialName("upperCorner", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("upperCorner")
@Serializable
data class UpperCorner(
    @XmlValue
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val values: List<Double>
)