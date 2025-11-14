package cloud.mallne.geokit.ogc.model.gml.member

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.geometry.AbstractCurveType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("curveMember", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("curveMember")
@Serializable
data class CurveMember(
    val curve: AbstractCurveType,
    override val arcrole: String? = null,
    override val href: String? = null,
    override val remoteSchema: String? = null,
    override val role: String? = null,
    override val title: String? = null,
) : MemberPrimitive