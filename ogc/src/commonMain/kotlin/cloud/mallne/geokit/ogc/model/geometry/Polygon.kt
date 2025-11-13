package cloud.mallne.geokit.ogc.model.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.Exterior
import cloud.mallne.geokit.ogc.model.Interior
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Polygon", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Polygon")
@Serializable
data class Polygon(
    @XmlElement
    val exterior: Exterior,
    @XmlElement
    val interior: Interior = Interior(),
    override val srsName: String? = null,
    //@XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
) : Geometry()