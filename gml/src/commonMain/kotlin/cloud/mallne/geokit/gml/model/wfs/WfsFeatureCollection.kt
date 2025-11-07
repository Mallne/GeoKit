package cloud.mallne.geokit.gml.model.wfs

import cloud.mallne.geokit.gml.model.Envelope
import cloud.mallne.geokit.gml.model.FeatureType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("FeatureCollection", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("FeatureCollection")
@Serializable
data class WfsFeatureCollection<T : FeatureType>(
    val timeStamp: String? = null,
    val numberMatched: Long? = null,
    val numberReturned: Long? = null,
    @XmlElement(true)
    val boundedBy: Envelope? = null,
    @XmlElement(true)
    val members: List<WfsFeatureMember<T>> = emptyList()
)