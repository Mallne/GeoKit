package cloud.mallne.geokit.gml.model.geometry

import cloud.mallne.geokit.gml.model.SurfaceMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GML MultiSurface geometry, holding one or more surfaces (Polygons).
 * <gml:MultiSurface srsName="..."><gml:surfaceMember>...</gml:surfaceMember>...</gml:MultiSurface>
 */
@Serializable
//@XmlSerialName("MultiSurface", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiSurface")
data class MultiSurface(
    override val srsName: String? = null,
    // Maps to a list of repeatable <surfaceMember> elements directly under the MultiSurface element.
    val surfaceMembers: List<SurfaceMember> = emptyList(),
    override val id: String? = null,
) : Geometry()