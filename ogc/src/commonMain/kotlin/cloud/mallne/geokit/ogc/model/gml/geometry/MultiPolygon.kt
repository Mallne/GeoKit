package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.member.PolygonMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("MultiPolygon", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiPolygon")
@Serializable
data class MultiPolygon(
    val polygonMember: List<PolygonMember>,
    override val srsName: String? = null,
    @XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
    override val srsDimension: Int? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
) : AbstractGeometricAggregateType()