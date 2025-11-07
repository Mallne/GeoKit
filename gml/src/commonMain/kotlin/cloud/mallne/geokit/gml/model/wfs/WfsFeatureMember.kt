package cloud.mallne.geokit.gml.model.wfs

import cloud.mallne.geokit.gml.model.FeatureType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("member", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("member")
@Serializable
data class WfsFeatureMember<T : FeatureType>(
    @XmlElement(true)
    val member: T
)