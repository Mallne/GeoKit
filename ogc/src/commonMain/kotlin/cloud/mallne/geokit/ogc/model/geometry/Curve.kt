package cloud.mallne.geokit.ogc.model.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.CurveSegments
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Curve", Namespaces.GML, Namespaces.Prefix.GML)
@Serializable
data class Curve(
    @XmlSerialName("segments", Namespaces.GML, Namespaces.Prefix.GML)
    val segments: CurveSegments,
    override val srsName: String? = null,
    override val id: String? = null
) : Geometry()