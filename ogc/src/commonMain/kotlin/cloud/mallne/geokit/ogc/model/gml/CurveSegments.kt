package cloud.mallne.geokit.ogc.model.gml

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.segment.AbstractCurveSegmentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("segments", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("segments")
@Serializable
data class CurveSegments(
    @XmlElement
    val segments: List<AbstractCurveSegmentType>
) : List<AbstractCurveSegmentType> by segments