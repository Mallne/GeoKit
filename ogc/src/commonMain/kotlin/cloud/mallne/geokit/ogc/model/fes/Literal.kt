package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@XmlSerialName("Literal", Namespaces.FES, Namespaces.Prefix.FES)
@SerialName("Literal")
@Serializable
data class Literal(
    @XmlValue val value: String
)