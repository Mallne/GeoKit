package cloud.mallne.geokit.ogc.model.fes

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlValue

//@XmlSerialName("ValueReference", Namespaces.FES, Namespaces.Prefix.FES)
@Serializable
data class ValueReference(
    @XmlValue val value: String
)