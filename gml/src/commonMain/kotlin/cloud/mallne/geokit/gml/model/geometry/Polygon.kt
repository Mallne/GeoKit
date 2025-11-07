package cloud.mallne.geokit.gml.model.geometry

import cloud.mallne.geokit.gml.model.Exterior
import cloud.mallne.geokit.gml.model.Interior
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("Polygon", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Polygon")
@Serializable
data class Polygon(
    @XmlElement(true)
    val exterior: Exterior,
    @XmlElement(true)
    val interior: Interior = Interior(),
    override val srsName: String? = null,
    //@XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
) : Geometry()