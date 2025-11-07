package cloud.mallne.geokit.gml.model.geometry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("MultiPolygon", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiPolygon")
@Serializable
data class MultiPolygon(
    @XmlElement(true)
    val polygonMember: List<Polygon>,
    override val srsName: String? = null,
    //@XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
) : Geometry()