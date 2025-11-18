package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.member.PointMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("MultiPoint", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiPoint")
@Serializable
data class MultiPoint(
    val pointMember: List<PointMember>,
    override val srsName: String? = null,
    @XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
    override val srsDimension: Int? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
) : AbstractGeometricAggregateType()