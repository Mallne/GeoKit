package cloud.mallne.geokit.ogc.model

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.ring.AbstractRing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("exterior", Namespaces.GML, Namespaces.Prefix.GML)
@Serializable
@SerialName("exterior")
data class Exterior(
    val ring: AbstractRing
)