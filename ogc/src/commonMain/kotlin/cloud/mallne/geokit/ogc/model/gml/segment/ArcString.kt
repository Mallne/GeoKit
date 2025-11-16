package cloud.mallne.geokit.ogc.model.gml.segment

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("ArcString", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("ArcString")
sealed class ArcString(
    @XmlSerialName("pos", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("pos")
    val pos: List<DirectPositionType>,
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("posList")
    val posList: List<DirectPositionType>,
    override val numDerivativeInterior: Int?,
    override val numDerivativesAtEnd: Int?,
    override val numDerivativesAtStart: Int?
) : AbstractCurveSegmentType() {
    override val positions: List<DirectPositionType>
        get() = pos + posList
}