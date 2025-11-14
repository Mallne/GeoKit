package cloud.mallne.geokit.ogc.model.gml.member

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.geometry.Polygon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName


@Serializable
@SerialName("polygonMember")
@XmlSerialName("polygonMember", Namespaces.GML, Namespaces.Prefix.GML)
data class PolygonMember(
    val polygon: Polygon,
    override val arcrole: String? = null,
    override val href: String? = null,
    override val remoteSchema: String? = null,
    override val role: String? = null,
    override val title: String? = null,
) : MemberPrimitive