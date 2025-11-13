package cloud.mallne.geokit.ogc.model.geometry

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("MultiLineString", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiLineString")
@Serializable
data class MultiLineString(
    @XmlElement(true)
    val lineStringMember: List<LineString>,
    override val srsName: String? = null,
    //@XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
) : Geometry()