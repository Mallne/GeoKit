package cloud.mallne.geokit.ogc.model.gml.member

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.geometry.LineString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * A wrapper class for a single member of a MultiSurface.
 * Represents <gml:surfaceMember>.
 */
@Serializable
@SerialName("lineStringMember")
@XmlSerialName("lineStringMember", Namespaces.GML, Namespaces.Prefix.GML)
data class LineStringMember(
    val lineString: LineString,
    override val arcrole: String? = null,
    override val href: String? = null,
    override val remoteSchema: String? = null,
    override val role: String? = null,
    override val title: String? = null,
) : MemberPrimitive