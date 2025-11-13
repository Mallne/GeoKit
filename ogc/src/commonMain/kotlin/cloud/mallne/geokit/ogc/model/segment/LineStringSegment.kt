package cloud.mallne.geokit.ogc.model.segment

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.SpaceSeparatedDoublesSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("LineStringSegment", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("LineStringSegment")
@Serializable
data class LineStringSegment(
    val interpolation: String = "linear",
    @XmlElement
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    override val posList: List<Double>
) : AbstractSegment()