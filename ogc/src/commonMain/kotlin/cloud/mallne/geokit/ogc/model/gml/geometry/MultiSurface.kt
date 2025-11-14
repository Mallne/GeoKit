package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.member.SurfaceMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * GML MultiSurface geometry, holding one or more surfaces (Polygons).
 * <gml:MultiSurface srsName="..."><gml:surfaceMember>...</gml:surfaceMember>...</gml:MultiSurface>
 */
@Serializable
@XmlSerialName("MultiSurface", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiSurface")
data class MultiSurface(
    override val srsName: String? = null,
    val surfaceMember: List<SurfaceMember> = emptyList(),
    override val id: String? = null,
    override val srsDimension: Int?,
    override val axisLabels: List<String>,
    override val gid: String?,
    override val uomLabels: List<String>,
) : AbstractGeometricAggregateType()