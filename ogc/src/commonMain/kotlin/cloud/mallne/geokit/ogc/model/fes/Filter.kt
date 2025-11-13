package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Filter", Namespaces.FES, Namespaces.Prefix.FES)
@Serializable
data class Filter(
    @XmlElement(true)
    val bbox: BBOX
)