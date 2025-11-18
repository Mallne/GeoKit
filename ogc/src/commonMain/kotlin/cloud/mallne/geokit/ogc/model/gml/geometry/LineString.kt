package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("LineString", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("LineString")
@Serializable
data class LineString(
    @XmlSerialName("pos", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("pos")
    val pos: List<DirectPositionType> = listOf(),
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("posList")
    val posList: List<DirectPositionType> = listOf(),
    override val srsName: String? = null,
    @XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
    override val srsDimension: Int? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
) : AbstractCurveType() {
    val positions: List<DirectPositionType>
        get() = pos + posList
}