package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("MultiPoint", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("MultiPoint")
@Serializable
data class MultiPoint(
    @XmlElement(true)
    val pointMember: List<Point>,
    override val srsName: String? = null,
    override val id: String? = null,
    override val srsDimension: Int?,
    override val axisLabels: List<String>,
    override val gid: String?,
    override val uomLabels: List<String>,
) : AbstractGeometricAggregateType()