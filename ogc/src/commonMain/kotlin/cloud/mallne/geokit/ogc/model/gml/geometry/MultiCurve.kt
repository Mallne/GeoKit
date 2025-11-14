package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.member.CurveMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("MultiCurve", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiCurve")
@Serializable
data class MultiCurve(
    val curveMember: List<CurveMember>,
    override val srsName: String? = null,
    override val id: String? = null,
    override val srsDimension: Int? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
) : AbstractGeometricAggregateType()