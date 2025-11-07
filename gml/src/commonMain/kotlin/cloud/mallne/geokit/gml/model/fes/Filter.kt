package cloud.mallne.geokit.gml.model.fes

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("Filter", Namespaces.FES, Namespaces.Prefix.FES)
@Serializable
data class Filter(
    @XmlElement(true)
    val bbox: BBOX
)