package cloud.mallne.geokit.gml.model.wfs

import cloud.mallne.geokit.gml.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/** WFS 2.0 FeatureCollection focusing on collection metadata and bounds. */
//@XmlSerialName("FeatureCollection", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("FeatureCollection")
@Serializable
data class FeatureCollection(
    val id: String? = null,
    val timeStamp: String? = null,
    val numberMatched: Long? = null,
    val numberReturned: Long? = null,
    @XmlElement(value = true)
    val boundedBy: WfsBoundedBy? = null,
    @XmlSerialName("member", Namespaces.WFS, Namespaces.Prefix.WFS)
    @XmlElement(true)
    val members: List<FeatureMember> = emptyList()
)