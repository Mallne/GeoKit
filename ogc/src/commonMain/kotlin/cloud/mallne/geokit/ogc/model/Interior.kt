package cloud.mallne.geokit.ogc.model

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.ring.AbstractRing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("interior", Namespaces.GML, Namespaces.Prefix.GML)
@Serializable
@SerialName("interior")
data class Interior(
    val rings: List<AbstractRing> = emptyList(),
) : List<AbstractRing> by rings