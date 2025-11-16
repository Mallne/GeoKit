package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import cloud.mallne.geokit.ogc.model.gml.member.PointMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("LinearRing", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("LinearRing")
@Serializable
data class LinearRing(
    @XmlSerialName("pos", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("pos")
    val pos: List<DirectPositionType> = listOf(),
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("posList")
    val posList: List<DirectPositionType> = listOf(),
    val pointMembers: List<PointMember> = listOf(),
    override val srsDimension: Int? = null,
    override val srsName: String? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
    override val id: String? = null,
) : AbstractRingType() {
    val positions: List<DirectPositionType>
        get() = pos + posList
}