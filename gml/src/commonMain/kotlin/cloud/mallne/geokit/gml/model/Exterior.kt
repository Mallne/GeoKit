package cloud.mallne.geokit.gml.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@XmlSerialName("exterior", Namespaces.GML, Namespaces.Prefix.GML)
@Serializable
@SerialName("exterior")
data class Exterior(
    val ring: LinearRing
)