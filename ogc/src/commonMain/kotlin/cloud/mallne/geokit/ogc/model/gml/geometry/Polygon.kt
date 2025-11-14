package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.AbstractRingPropertyType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Polygon", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Polygon")
@Serializable
data class Polygon(
    @XmlSerialName("exterior", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("exterior")
    val exterior: AbstractRingPropertyType,
    @XmlSerialName("interior", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("interior")
    val interior: List<AbstractRingPropertyType> = listOf(),
    override val srsName: String? = null,
    override val id: String? = null,
    override val srsDimension: Int? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
) : AbstractSurfaceType()