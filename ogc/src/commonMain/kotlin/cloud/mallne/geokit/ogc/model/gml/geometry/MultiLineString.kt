package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.member.LineStringMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("MultiLineString", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiLineString")
@Serializable
data class MultiLineString(
    val lineStringMember: List<LineStringMember>,
    override val srsName: String? = null,
    override val id: String? = null,
    override val srsDimension: Int? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
) : AbstractGeometricAggregateType()