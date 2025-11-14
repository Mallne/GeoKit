package cloud.mallne.geokit.ogc.model.gml.member

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.geometry.AbstractSurfaceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * A wrapper class for a single member of a MultiSurface.
 * Represents <gml:surfaceMember>.
 */
@Serializable
@SerialName("surfaceMember")
@XmlSerialName("surfaceMember", Namespaces.GML, Namespaces.Prefix.GML)
data class SurfaceMember(
    val surface: AbstractSurfaceType,
    override val arcrole: String? = null,
    override val href: String? = null,
    override val remoteSchema: String? = null,
    override val role: String? = null,
    override val title: String? = null,
) : MemberPrimitive