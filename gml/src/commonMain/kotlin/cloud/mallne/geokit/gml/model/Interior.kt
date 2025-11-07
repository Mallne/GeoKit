package cloud.mallne.geokit.gml.model

import cloud.mallne.geokit.gml.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("interior", Namespaces.GML, Namespaces.Prefix.GML)
@Serializable
@SerialName("interior")
data class Interior(
    val rings: List<LinearRing> = emptyList(),
) : List<LinearRing> by rings