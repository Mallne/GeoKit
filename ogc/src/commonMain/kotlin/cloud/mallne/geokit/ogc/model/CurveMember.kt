package cloud.mallne.geokit.ogc.model

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.geometry.Curve
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("curveMember", Namespaces.GML, Namespaces.Prefix.GML)
@Serializable
data class CurveMember(
    @XmlSerialName("Curve", Namespaces.GML, Namespaces.Prefix.GML)
    val curve: Curve
)