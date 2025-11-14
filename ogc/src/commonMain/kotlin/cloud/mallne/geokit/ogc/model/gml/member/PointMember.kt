package cloud.mallne.geokit.ogc.model.gml.member

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.geometry.Point
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName


@Serializable
@SerialName("pointMember")
@XmlSerialName("pointMember", Namespaces.GML, Namespaces.Prefix.GML)
data class PointMember(
    val point: Point,
    override val arcrole: String? = null,
    override val href: String? = null,
    override val remoteSchema: String? = null,
    override val role: String? = null,
    override val title: String? = null,
) : MemberPrimitive