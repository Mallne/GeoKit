package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Point", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Point")
@Serializable
data class Point(
    @XmlSerialName("pos", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("pos")
    val pos: DirectPositionType,
    override val srsName: String? = null,
    override val id: String? = null,
    override val srsDimension: Int? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
) : AbstractGeometricPrimitiveType()