package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@XmlSerialName("ValueReference", Namespaces.FES, Namespaces.Prefix.FES)
@Serializable
data class ValueReference(
    @XmlValue val value: String
)