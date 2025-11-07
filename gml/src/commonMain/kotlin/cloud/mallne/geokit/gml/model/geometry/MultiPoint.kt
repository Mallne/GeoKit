package cloud.mallne.geokit.gml.model.geometry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("MultiPoint", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiPoint")
@Serializable
data class MultiPoint(
    @XmlElement(true)
    val pointMember: List<Point>,
    override val srsName: String? = null,
    //@XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
) : Geometry()