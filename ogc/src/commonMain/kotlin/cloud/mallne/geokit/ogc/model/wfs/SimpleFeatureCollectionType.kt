package cloud.mallne.geokit.ogc.model.wfs

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
sealed class SimpleFeatureCollectionType {
    @XmlElement(value = true)
    abstract val boundedBy: WfsBoundedBy?
}