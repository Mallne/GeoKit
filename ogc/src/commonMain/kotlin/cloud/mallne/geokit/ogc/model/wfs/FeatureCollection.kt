package cloud.mallne.geokit.ogc.model.wfs

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/** WFS 2.0 `FeatureCollectionType` response metadata and collection contents. */
@XmlSerialName("FeatureCollection", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("FeatureCollection")
@Serializable
data class FeatureCollection(
    val timeStamp: String,
    /** WFS 2.0 permits a non-negative integer or the literal `unknown`. */
    val numberMatched: String,
    val numberReturned: Long,
    @XmlSerialName("next", "", "")
    @XmlElement(false)
    val next: String? = null,
    @XmlSerialName("previous", "", "")
    @XmlElement(false)
    val previous: String? = null,
    @XmlSerialName("lockId", "", "")
    @XmlElement(false)
    val lockId: String? = null,
    @XmlElement(value = true)
    override val boundedBy: WfsBoundedBy? = null,
    @XmlSerialName("member", Namespaces.WFS, Namespaces.Prefix.WFS)
    @XmlElement(true)
    val members: List<FeatureMember> = emptyList()
) : SimpleFeatureCollectionType()
