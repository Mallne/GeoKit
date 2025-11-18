package cloud.mallne.geokit.ogc.model.gml.segment

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.CurveInterpolationType
import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import cloud.mallne.geokit.ogc.model.gml.member.PointMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("LineStringSegment", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("LineStringSegment")
@Serializable
data class LineStringSegment(
    @XmlElement(false)
    val interpolation: CurveInterpolationType? = null,
    @XmlSerialName("pos", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("pos")
    val pos: List<DirectPositionType> = listOf(),
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("posList")
    val posList: List<DirectPositionType> = listOf(),
    val pointMembers: List<PointMember> = listOf(),
    override val numDerivativeInterior: Int? = null,
    override val numDerivativesAtEnd: Int? = null,
    override val numDerivativesAtStart: Int? = null,
) : AbstractCurveSegmentType() {
    @Transient
    override val positions: List<DirectPositionType>
        get() = pos + posList
}