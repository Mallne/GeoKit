package cloud.mallne.geokit.ogc.model.ring

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.CurveMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/** Complex GML Ring, consisting of CurveMember(s). */
@XmlSerialName("Ring", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Ring")
@Serializable
data class Ring(
    val curveMember: List<CurveMember>,
    val id: String? = null,
    val srsName: String? = null
) : AbstractRing()
