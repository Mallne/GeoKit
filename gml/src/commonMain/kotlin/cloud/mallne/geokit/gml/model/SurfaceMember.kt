package cloud.mallne.geokit.gml.model

import cloud.mallne.geokit.gml.model.geometry.Geometry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A wrapper class for a single member of a MultiSurface.
 * Represents <gml:surfaceMember>.
 */
@Serializable
@SerialName("surfaceMember")
//@XmlSerialName("surfaceMember", Namespaces.GML, Namespaces.Prefix.GML)
data class SurfaceMember(
    val geometry: Geometry
)