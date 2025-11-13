package cloud.mallne.geokit.ogc.model

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.geometry.Geometry
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
    val geometry: Geometry
)